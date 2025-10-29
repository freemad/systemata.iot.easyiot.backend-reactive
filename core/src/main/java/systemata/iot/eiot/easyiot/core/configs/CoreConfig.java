package systemata.iot.eiot.easyiot.core.configs;

import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties({CoreProps.class})
public class CoreConfig {
}