package systemata.iot.eiot.easyiot.common.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application.common", ignoreUnknownFields = false)
public class CommonProps {
}
