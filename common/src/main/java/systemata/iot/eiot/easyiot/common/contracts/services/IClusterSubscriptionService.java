package systemata.iot.eiot.easyiot.common.contracts.services;

import reactor.core.publisher.Flux;

import java.io.Serializable;

public interface IClusterSubscriptionService<T extends Serializable> {
    /**
     * Subscribes all interested nodes to a topic.
     * Returns a Flux of payloads coming from MQTT or another shared source (Kafka, Redis pub/sub, etc.)
     */
    Flux<T> subscribe(String topic);

    /**
     * Unsubscribes this node/session from topic updates.
     */
    void unsubscribe(String topic);
}