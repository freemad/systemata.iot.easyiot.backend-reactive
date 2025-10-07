package systemata.iot.eiot.core.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RuleEngineSample {

    @KafkaListener(topics = "device-data", groupId = "rule-engine")
    public void process(ConsumerRecord<String, String> record) {
        String value = record.value();
        System.out.println("RuleEngine received: " + value);

        // Simple rule sample
        // todo: load user js script & apply the rule
        if (value.contains("\"temperature\":") && value.contains("35")) {
            System.out.println("🚨 ALERT: High temperature detected!");
            // todo: persist alert, send WS notification, what user wants
        }
    }
}
