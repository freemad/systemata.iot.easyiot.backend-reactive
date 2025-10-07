package systemata.iot.eiot.easyiot.edgemqtt.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import systemata.iot.eiot.easyiot.common.configs.CommonProps;
import systemata.iot.eiot.easyiot.common.domain.models.Mqtt2KafkaTopicMapping;
import systemata.iot.eiot.easyiot.common.domain.models.MqttMessageEnvelope;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Mqtt2KafkaBridgeService {

    private final MqttService mqttService;
    private final CommonProps commonProps;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final List<Disposable> subscriptions = new ArrayList<>();

    @PostConstruct
    public void start() throws MqttException {
        List<Mqtt2KafkaTopicMapping> mappings = commonProps.getMqttKafkaBridge().getMqtt2KafkaTopicMappings();

        for (Mqtt2KafkaTopicMapping mapping : mappings) {
            String mqttTopic = mapping.getMqttTopic();   // e.g. "devices/+/telemetry"
            String kafkaTopic = mapping.getKafkaTopic();

            // subscribe once per mapping
            Flux<MqttMessageEnvelope> stream = mqttService.subscribe(mqttTopic);

            Disposable sub = stream.subscribe(mme -> {
                // Extra regex check if you stored it as Pattern
                if (mapping.getMqttTopicPattern().matcher(mme.getTopic()).matches()) {
                    // use deviceId as key if topic structure is devices/{id}/xxx
                    String[] parts = mme.getTopic().split("/");
                    String key = (parts.length >= 2) ? parts[1] : null;

                    kafkaTemplate.send(new ProducerRecord<>(kafkaTopic, key, mme.getPayload()));
                }
            });

            subscriptions.add(sub);
            log.info("Bridging MQTT [{}] → Kafka [{}]", mqttTopic, kafkaTopic);
        }
    }

    @PreDestroy
    public void stop() {
        subscriptions.forEach(Disposable::dispose);
        log.info("Stopped MQTT → Kafka bridge");
    }
}
