package systemata.iot.eiot.easyiot.common.configs;

import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties(CommonProps.class)
public class CommonConfig {
}
