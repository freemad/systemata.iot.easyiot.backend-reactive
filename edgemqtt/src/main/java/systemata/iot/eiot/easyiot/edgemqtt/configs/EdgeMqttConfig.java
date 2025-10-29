package systemata.iot.eiot.easyiot.edgemqtt.configs;

import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties({EdgeMqttProps.class})
public class EdgeMqttConfig {
}