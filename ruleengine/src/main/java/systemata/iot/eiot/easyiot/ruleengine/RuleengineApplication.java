package systemata.iot.eiot.easyiot.ruleengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("systemata.iot")
public class RuleengineApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuleengineApplication.class, args);
    }

}
