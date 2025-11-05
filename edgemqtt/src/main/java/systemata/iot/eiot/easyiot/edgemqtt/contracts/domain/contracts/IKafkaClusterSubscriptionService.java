package systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.contracts;

import java.util.function.Consumer;

public interface IKafkaClusterSubscriptionService {
    void publish(String topic, Object message);

    void subscribe(String topic, Consumer<Object> listener);

    void unsubscribe(String topic, Consumer<Object> listener);

    void broadcast(String topic, Object message); // optional alias
}
