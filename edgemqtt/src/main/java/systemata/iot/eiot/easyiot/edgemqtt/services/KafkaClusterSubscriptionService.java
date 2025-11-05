package systemata.iot.eiot.easyiot.edgemqtt.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.DeviceEvent;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Cluster-wide subscription service using Kafka as the shared event bus.
 * This allows WebSocket clients on different app nodes to receive MQTT-derived telemetry/control/command events.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaClusterSubscriptionService {

    private final KafkaSender<String, DeviceEvent> sender;
    private final KafkaReceiver<String, DeviceEvent> receiver;

    private final Map<String, Sinks.Many<DeviceEvent>> topicSinks = new ConcurrentHashMap<>();
    private final Map<String, Disposable> topicSubscriptions = new ConcurrentHashMap<>();

    /**
     * Subscribe to topic -> Flux<DeviceEvent>
     */
    public Flux<DeviceEvent> subscribe(String topic) {
        return topicSinks
                .computeIfAbsent(topic, this::createTopicSink)
                .asFlux()
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(sub -> log.info("webSocket client subscribed to kafka topic {}", topic))
                .doFinally(sig -> log.debug("webSocket stream for topic {} finished with {}", topic, sig));
    }

    /**
     * Publish to topic
     */
    public Flux<SenderResult<Void>> publish(String topic, DeviceEvent event) {
        SenderRecord<String, DeviceEvent, Void> record = SenderRecord.create(
                topic,
                null,  // partition
                null,  // timestamp
                event.getDeviceId().toString(), // key
                event, // value
                null   // correlation metadata
        );

        return sender.send(Flux.just(record))
                .doOnNext(r -> log.debug("kafka published: {}", event))
                .doOnError(e -> log.error("kafka publish failed: {}", e.getMessage(), e));
    }

    private Sinks.Many<DeviceEvent> createTopicSink(String topic) {
        Sinks.Many<DeviceEvent> sink = Sinks.many().multicast().onBackpressureBuffer();

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

    private Flux<DeviceEvent> createTopicFlux(String topic) {
        log.info("subscribing KafkaReceiver (manual ack) to topic {}", topic);

        return receiver
                .receive() // Flux<ReceiverRecord<K,V>>
                .filter(record -> record.topic().equals(topic))
                .map(record -> {
                    record.receiverOffset().acknowledge();
                    return record.value();
                })
                .doOnNext(msg -> log.debug("kafka consumed from {} -> {}", topic, msg))
                .doOnError(e -> log.error("kafka consumption error for {}: {}", topic, e.getMessage()))
                .doFinally(sig -> log.info("kafka flux for {} completed: {}", topic, sig));
    }

    public void cleanup() {
        log.info("cleaning up kafkaClusterSubscriptionService...");
        topicSubscriptions.values().forEach(Disposable::dispose);
        topicSubscriptions.clear();
        topicSinks.clear();
        log.info("KafkaClusterSubscriptionService cleaned up!");
    }
}