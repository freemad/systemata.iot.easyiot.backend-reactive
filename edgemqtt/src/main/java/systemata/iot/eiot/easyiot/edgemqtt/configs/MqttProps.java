package systemata.iot.eiot.easyiot.edgemqtt.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import systemata.iot.eiot.easyiot.common.domain.models.Mqtt2KafkaTopicMapping;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "application.mqtt-config")
public class MqttProps {

    private final ConnectionProps connectionProps =  new ConnectionProps();
    private final StreamProps streamProps = new StreamProps();
    private final MqttKafkaBridge mqttKafkaBridge = new MqttKafkaBridge();

    @Data
    public static class ConnectionProps {
        private String broker;
        private String clientId;
        private Boolean autoReconnect;
        private Boolean cleanSession;
        private Integer keepAliveInterval;
        private Integer connectionTimeout;
        private Integer reconnectInterval;
        private Integer reconnectAttempts;
        private String username;
        private String password;
        private Integer defaultQos;
        private List<String> topics;
    }

    @Data
    public static class StreamProps {
        private Boolean autoCancel;
        private Integer bufferSize;
        private Integer timeoutInMin;
    }

    @Data
    public static class MqttKafkaBridge {
        private List<Mqtt2KafkaTopicMapping> topicMappings;
    }

}
