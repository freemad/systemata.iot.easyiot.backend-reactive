package systemata.iot.eiot.easyiot.edgemqtt.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import systemata.iot.eiot.easyiot.common.configs.CommonProps;
import systemata.iot.eiot.easyiot.common.domain.models.MqttMessageEnvelope;
import systemata.iot.eiot.easyiot.common.exceptions.BusinessException;
import systemata.iot.eiot.easyiot.common.exceptions.CommonErrorCode;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttService {

    private final CommonProps commonProps;
    private final Integer bufferSize = 10_000;
    private final Boolean autoCancel = false;
    private final Integer streamTimeoutDurationInMin = 30;

    private MqttAsyncClient client;
    private MqttConnectOptions options;
    private Integer defaultQos;

    private final Map<String, Sinks.Many<MqttMessageEnvelope>> topicSinks = new ConcurrentHashMap<>();
    private final Set<String> subscribedTopics = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void init() throws MqttException {
        var mqttConfig = commonProps.getMqttConfig();
        defaultQos = mqttConfig.getDefaultQos();

        options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(10);
        client = new MqttAsyncClient(mqttConfig.getBroker(), mqttConfig.getClientId(), new MemoryPersistence());
        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                log.info("mqtt connected. reconnect: {}, uri: {}", reconnect, serverURI);
                if (reconnect) {
                    resubscribeAll();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                log.warn("mqtt connection lost: {}", cause.getMessage(), cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                // route message into sink for this topic
                Sinks.Many<MqttMessageEnvelope> sink = getTopicSink(topic);
                if (sink != null) {
                    sink.tryEmitNext(new MqttMessageEnvelope(
                            topic,
                            message.getQos(),
                            new String(message.getPayload()),
                            message.isRetained(),
                            Instant.now(), null));
                } else {
                    log.debug("no sink for topic {}, message dropped", topic);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                log.debug("mqtt delivery complete: {}", token.getMessageId());
            }
        });
        client.connect().waitForCompletion();
    }

    public Flux<MqttMessageEnvelope> subscribe(String topic) throws MqttException {
        // ensure sink exists for topic
        Sinks.Many<MqttMessageEnvelope> sink = createOrReuseSink(topic);

        // subscribe at broker once
        if (subscribedTopics.add(topic)) {
            client.subscribe(topic, defaultQos);
            log.info("subscribed to mqtt topic {}", topic);
        }

        return sink.asFlux();
    }

    public Mono<Void> publish(String topic, String payload, int qos, boolean retained) {
        return Mono.fromRunnable(() -> {
            try {
                client.publish(topic, payload.getBytes(), qos, retained);
            } catch (MqttException e) {
                throw new BusinessException(CommonErrorCode.DATA_TRANSFER_EXCEPTION, "failed to publish to topic: " + topic, e);
            }
        });
    }

    public Mono<Void> publish(MqttMessageEnvelope messageEnvelope) {
        return publish(messageEnvelope.getTopic(), messageEnvelope.getPayload(), messageEnvelope.getQos(), messageEnvelope.getRetained());
    }

    private void resubscribeAll() {
        subscribedTopics.forEach(topic -> {
            try {
                client.subscribe(topic, defaultQos);
                log.info("resubscribed to {}", topic);
            } catch (MqttException e) {
                log.error("failed to resubscribe {}", topic, e);
            }
        });
    }

    public Flux<MqttMessageEnvelope> stream(String topic) {
        // optional heartbeat to keep SSE/WebSocket alive behind proxies
        Sinks.Many<MqttMessageEnvelope> sink = getTopicSink(topic);
        if (sink != null) {
            return sink.asFlux()
                    .timeout(Duration.ofMinutes(streamTimeoutDurationInMin))
                    .onErrorResume(e -> Flux.empty());
        }
        return null;
    }

    @PreDestroy
    public void shutdown() throws MqttException {
        if (client != null && client.isConnected()) {
            client.disconnect();
            client.close();
        }
    }

    private Sinks.Many<MqttMessageEnvelope> createOrReuseSink(String topic) {
        return topicSinks.computeIfAbsent(
                topic,
                t -> Sinks.many()
                        .multicast()
                        .onBackpressureBuffer(bufferSize, autoCancel)
        );
    }

    private Sinks.Many<MqttMessageEnvelope> getTopicSink(String topic) {
        return topicSinks.get(topic);
    }
}
