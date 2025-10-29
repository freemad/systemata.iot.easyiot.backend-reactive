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
import reactor.core.scheduler.Schedulers;
import systemata.iot.eiot.easyiot.common.domain.models.MqttMessageEnvelope;
import systemata.iot.eiot.easyiot.common.exceptions.BusinessException;
import systemata.iot.eiot.easyiot.common.exceptions.CommonErrorCode;
import systemata.iot.eiot.easyiot.edgemqtt.configs.EdgeMqttProps;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class MqttService {

    private final EdgeMqttProps edgeMqttProps;

    private MqttAsyncClient client;
    private MqttConnectOptions options;

    // Sink per topic pattern
    private final Map<String, Sinks.Many<MqttMessageEnvelope>> topicSinks = new ConcurrentHashMap<>();

    // Cached compiled regex patterns for fast topic matching
    private final Map<String, Pattern> topicPatterns = new ConcurrentHashMap<>();

    // Active subscribed MQTT topics
    private final Set<String> subscribedTopics = ConcurrentHashMap.newKeySet();

    @PostConstruct
    public void init() throws MqttException {
        options = new MqttConnectOptions();
        options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        options.setCleanSession(edgeMqttProps.getMqttProps().getCleanSession());
        options.setAutomaticReconnect(edgeMqttProps.getMqttProps().getAutoReconnect());
        options.setKeepAliveInterval(edgeMqttProps.getMqttProps().getKeepAliveInterval());
        options.setConnectionTimeout(edgeMqttProps.getMqttProps().getConnectionTimeout());

        client = new MqttAsyncClient(
                edgeMqttProps.getMqttProps().getBroker(),
                edgeMqttProps.getMqttProps().getClientId(),
                new MemoryPersistence()
        );

        client.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                log.info("MQTT connected. reconnect={}, uri={}", reconnect, serverURI);
                if (reconnect) resubscribeAll();
            }

            @Override
            public void connectionLost(Throwable cause) {
                log.warn("MQTT connection lost: {}", cause.getMessage(), cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                handleIncomingMessage(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                log.debug("MQTT delivery complete: {}", token.getMessageId());
            }
        });

        client.connect().waitForCompletion();
    }

    // ================================
    // Message handling
    // ================================

    private void handleIncomingMessage(String topic, MqttMessage message) {
        MqttMessageEnvelope envelope = new MqttMessageEnvelope(
                topic,
                message.getQos(),
                new String(message.getPayload(), StandardCharsets.UTF_8),
                message.isRetained(),
                Instant.now(),
                null
        );

        // Emit to all sinks that match
        topicSinks.forEach((pattern, sink) -> {
            Pattern compiled = topicPatterns.computeIfAbsent(pattern, this::compileMqttPattern);
            if (compiled.matcher(topic).matches()) {
                sink.tryEmitNext(envelope);
            }
        });
    }

    // Compile MQTT wildcard pattern into a regex
    private Pattern compileMqttPattern(String mqttPattern) {
        // Escape regex special chars except / + #
        String regex = mqttPattern
                .replaceAll("([.()|^$])", "\\\\$1")  // escape . ( ) | ^ $
                .replace("+", "[^/]+")              // + → match one topic level
                .replace("#", ".+");                // # → match any number of levels
        return Pattern.compile("^" + regex + "$");
    }

    // ================================
    // Subscription handling
    // ================================

    public Flux<MqttMessageEnvelope> subscribe(String topic) throws MqttException {
        Sinks.Many<MqttMessageEnvelope> sink = createOrReuseSink(topic);
        if (subscribedTopics.add(topic)) {
            client.subscribe(topic, edgeMqttProps.getMqttProps().getDefaultQos());
            log.info("subscribed to MQTT topic {}", topic);
        }
        return sink.asFlux()
                .publishOn(Schedulers.boundedElastic())
                .doOnCancel(() -> cleanupSink(topic))
                .timeout(Duration.ofMinutes(edgeMqttProps.getStreamProps().getTimeoutInMin()))
                .onErrorResume(e -> {
                    log.debug("sse stream error for topic {}: {}", topic, e.getMessage());
                    cleanupSink(topic);
                    return Flux.empty();
                });
    }

    public Flux<MqttMessageEnvelope> stream(String topic) {
        try {
            return subscribe(topic);
        } catch (MqttException e) {
            log.error("failed to subscribe to topic {}: {}", topic, e.getMessage());
            return Flux.error(e);
        }
    }

    private Sinks.Many<MqttMessageEnvelope> createOrReuseSink(String topic) {
        return topicSinks.computeIfAbsent(topic, t -> {
            log.info("creating new sink for topic pattern: {}", t);
            topicPatterns.put(t, compileMqttPattern(t));
            return Sinks.many()
                    .multicast()
                    .onBackpressureBuffer(
                            edgeMqttProps.getStreamProps().getBufferSize(),
                            edgeMqttProps.getStreamProps().getAutoCancel()
                    );
        });
    }

    private void cleanupSink(String topic) {
        log.info("cleaning up sink for topic {}", topic);
        topicSinks.remove(topic);
        topicPatterns.remove(topic);
        subscribedTopics.remove(topic);
    }

    private void resubscribeAll() {
        subscribedTopics.forEach(t -> {
            try {
                client.subscribe(t, edgeMqttProps.getMqttProps().getDefaultQos());
                log.info("resubscribed to {}", t);
            } catch (MqttException e) {
                log.error("failed to resubscribe {}", t, e);
            }
        });
    }

    // ================================
    // Publishing
    // ================================

    public Mono<Void> publish(String topic, String payload, int qos, boolean retained) {
        return Mono.fromRunnable(() -> {
            try {
                client.publish(topic, payload.getBytes(StandardCharsets.UTF_8), qos, retained);
                log.debug("published MQTT message to {}, qos={}, retained={}", topic, qos, retained);
            } catch (MqttException e) {
                throw new BusinessException(CommonErrorCode.DATA_TRANSFER_EXCEPTION,
                        "failed to publish to topic: " + topic, e);
            }
        });
    }

    // ================================
    // Shutdown
    // ================================

    @PreDestroy
    public void shutdown() throws MqttException {
        log.info("shutting down MQTT service...");
        if (client != null && client.isConnected()) {
            client.disconnect();
            client.close();
        }
        topicSinks.clear();
        topicPatterns.clear();
        subscribedTopics.clear();
    }
}
