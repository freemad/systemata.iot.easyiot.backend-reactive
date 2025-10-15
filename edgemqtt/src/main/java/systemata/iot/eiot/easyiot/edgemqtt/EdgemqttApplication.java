package systemata.iot.eiot.easyiot.edgemqtt;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Hooks;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration.class
})
@ComponentScan("systemata.iot")
public class EdgemqttApplication {

    public static void main(String[] args) {
        SpringApplication.run(EdgemqttApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Hooks.onOperatorDebug();
    }
}
