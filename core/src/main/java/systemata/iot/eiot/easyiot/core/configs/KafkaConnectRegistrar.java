package systemata.iot.eiot.easyiot.core.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConnectRegistrar {

    private final CoreProps coreProps;
    private final RestTemplate restTemplate = new RestTemplate();

    @Bean
    public CommandLineRunner registerKafkaConnector() {
        return args -> {
            try {
                // 1 Load the YAML connector config
                Map<String, Object> connectorSpec = getConnectorSpec();

                // 2️Send the POST request to Kafka Connect REST API
                String connectUrl = coreProps.getKafkaConnectProps().getConnectUrl(); //"http://kafka-connect:8083/connectors";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                ObjectMapper jsonMapper = new ObjectMapper();
                String body = jsonMapper.writeValueAsString(connectorSpec);

                log.info("registering kafka connector at {} -> {}", connectUrl, connectorSpec.get("name"));
                ResponseEntity<String> response = restTemplate.exchange(
                        connectUrl,
                        HttpMethod.POST,
                        new HttpEntity<>(body, headers),
                        String.class
                );
                log.info("kafka connector registration response: {}", response.getBody());
            } catch (Exception e) {
                log.error("failed to register kafka connector: {}", e.getMessage(), e);
            }
        };
    }

    private Map<String, Object> getConnectorSpec() throws IOException {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        InputStream input = new ClassPathResource(coreProps.getKafkaConnectProps().getRegisterConfigPath()).getInputStream();
        return yamlMapper.readValue(input, Map.class);
    }
}
