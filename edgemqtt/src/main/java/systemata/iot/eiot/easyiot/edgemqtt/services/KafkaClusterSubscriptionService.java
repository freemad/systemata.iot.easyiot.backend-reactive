package systemata.iot.eiot.easyiot.edgemqtt.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsOutgoing;
import systemata.iot.eiot.easyiot.edgemqtt.domain.enums.WsMsgType;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Cluster-wide subscription service using Kafka as the shared event bus.
 * This allows WebSocket clients on different app nodes to receive MQTT-derived telemetry or control events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaClusterSubscriptionService {

    private final KafkaSender<String, String> sender;
    private final KafkaReceiver<String, String> receiver;

    private final Map<String, Sinks.Many<WsOutgoing>> topicSinks = new ConcurrentHashMap<>();
    private final Map<String, Disposable> topicSubscriptions = new ConcurrentHashMap<>();

    /**
     * Subscribe to topic -> Flux<WsOutgoing>
     */
    public Flux<WsOutgoing> subscribe(String topic) {
        return topicSinks.computeIfAbsent(topic, this::createTopicSink).asFlux();
    }

    /**
     * Publish to topic
     */
    public Flux<SenderResult<Void>> publish(WsOutgoing outgoing) {
        SenderRecord<String, String, Void> record = SenderRecord.create(
                outgoing.getTopic(),
                null,
                null,
                outgoing.getTopic(),
                outgoing.getPayload(),
                null
        );

        return sender.send(Flux.just(record))
                .doOnNext(r -> log.debug("Kafka published: {}", outgoing))
                .doOnError(e -> log.error("Kafka publish failed: {}", e.getMessage()));
    }

    private Sinks.Many<WsOutgoing> createTopicSink(String topic) {
        Sinks.Many<WsOutgoing> sink = Sinks.many().multicast().onBackpressureBuffer();

        Disposable disposable = createTopicFlux(topic)
                .subscribe(
                        sink::tryEmitNext,
                        err -> {
                            log.error("Kafka consumer for topic {} failed: {}", topic, err.getMessage());
                            sink.tryEmitError(err);
                        },
                        sink::tryEmitComplete
                );

        topicSubscriptions.put(topic, disposable);
        return sink;
    }

    private Flux<WsOutgoing> createTopicFlux(String topic) {
        log.info("Subscribing KafkaReceiver to topic {}", topic);

        return receiver
                .receiveAutoAck()
                .flatMap(batchFlux -> batchFlux)
                .filter(r -> r.topic().equals(topic))
                .map(r -> new WsOutgoing(
                        WsMsgType.WS_MSG_TYPE_KAFKA_MSG,
                        r.topic(),
                        r.value(),
                        1,
                        false,
                        Instant.now()
                ))
                .doOnNext(msg -> log.debug("Kafka consumed from {} -> {}", topic, msg))
                .doOnError(e -> log.error("Kafka consumption error for {}: {}", topic, e.getMessage()));
    }

    public void cleanup() {
        topicSubscriptions.values().forEach(Disposable::dispose);
        topicSubscriptions.clear();
        topicSinks.clear();
    }
}