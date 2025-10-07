package systemata.iot.eiot.common.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import systemata.iot.eiot.common.domain.models.Mqtt2KafkaTopicMapping;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class CommonProps {

    private final MqttConfig mqttConfig = new MqttConfig();
    private final MqttKafkaBridge mqttKafkaBridge = new MqttKafkaBridge();

    @Data
    public static class MqttConfig {
        private String broker;
        private String clientId;
        private Integer defaultQos;
        private String username;
        private String password;
        private List<String> topics;
    }

    @Data
    public static class MqttKafkaBridge {
        private List<Mqtt2KafkaTopicMapping> mqtt2KafkaTopicMappings;
    }
}
