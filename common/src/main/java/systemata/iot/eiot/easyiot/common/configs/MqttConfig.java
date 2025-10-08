//package systemata.iot.eiot.easyiot.common.configs;
//
//import lombok.RequiredArgsConstructor;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class MqttConfig {
//
//    private final ApplicationProps applicationProps;
//
//    @Bean
//    public MqttClient mqttClient() throws MqttException {
//        ApplicationProps.MqttConfig mqttConfig = applicationProps.getMqttConfig();
//        MqttClient client = new MqttClient(mqttConfig.getBroker(), mqttConfig.getClientId());
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setAutomaticReconnect(true);
//        options.setCleanSession(true);
//        client.connect(options);
//        return client;
//    }
//}
