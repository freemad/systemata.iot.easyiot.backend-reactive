package systemata.iot.eiot.common.domain.models;

import jakarta.annotation.PostConstruct;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class Mqtt2KafkaTopicMapping {
    private String mqttTopic;     // raw from config, e.g. "devices/+/telemetry"
    private String kafkaTopic;
    private Pattern mqttTopicPattern;

    @PostConstruct
    public void init() {
        // Convert MQTT wildcards (+, #) into regex for matching
        String regex = mqttTopic
                .replace("+", "[^/]+")   // + → one segment
                .replace("#", ".+");     // # → all remaining
        this.mqttTopicPattern = Pattern.compile(regex);
    }
}
