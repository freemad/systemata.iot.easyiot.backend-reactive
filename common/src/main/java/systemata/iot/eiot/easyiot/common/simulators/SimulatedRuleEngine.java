//package systemata.iot.eiot.easyiot.common.simulators;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SimulatedRuleEngine {
//
//    @KafkaListener(topics = "device-data", groupId = "rule-engine")
//    public void process(ConsumerRecord<String, String> record) {
//        String value = record.value();
//        System.out.println("RuleEngine received: " + value);
//
//        // Simple rule example
//        if (value.contains("\"temperature\":") && value.contains("35")) {
//            System.out.println("🚨 ALERT: High temperature detected!");
//            // Here you could: persist alert, send WS notification, etc.
//        }
//    }
//}
