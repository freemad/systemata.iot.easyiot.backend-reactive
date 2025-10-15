package systemata.iot.eiot.easyiot.common.domain.models;

import jakarta.annotation.PostConstruct;
import lombok.Data;

import java.util.regex.Pattern;

@Data
public class Mqtt2KafkaTopicMapping {
    private String mqttTopic;     // raw from config, e.g. "devices/+/telemetry"
    private String kafkaTopic;

    public Pattern getMqttTopicPattern() {
        String regex = mqttTopic
                .replace("+", "[^/]+")   // + → one segment
                .replace("#", ".+");     // # → all remaining
        return Pattern.compile(regex);
    }
}
