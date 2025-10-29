package systemata.iot.eiot.easyiot.core.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.core")
public class CoreProps {

    private final KafkaConnectProps kafkaConnectProps =  new KafkaConnectProps();

    @Data
    public static class KafkaConnectProps {
        private String registerConfigPath;
        private String connectUrl;
    }
}
