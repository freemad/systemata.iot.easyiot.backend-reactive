//package systemata.iot.eiot.easyiot.common.simulators;
//
//import lombok.RequiredArgsConstructor;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import systemata.iot.eiot.easyiot.common.configs.ApplicationProps;
//import systemata.iot.eiot.easyiot.common.services.DeviceManagerService;
//
//import java.util.ArrayList;
//import java.util.Random;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class DeviceSimulator
//        implements CommandLineRunner {
//
//    private final ApplicationProps applicationProps;
//    private final DeviceManagerService deviceManagerService;
//
//    @Override
//    public void  run(String[] args) throws Exception {
//        var arg0 = args[0];
//        var deviceCount = arg0 != null ?Integer.parseInt(args[0]) : 5;
//        var mqttConfig = applicationProps.getMqttConfig();
//        String broker = mqttConfig.getBroker();
//        String clientId = "simulator-" + UUID.randomUUID();
//        try (MqttClient client = new MqttClient(broker, clientId)) {
//            client.connect();
//            var deviceIds = new ArrayList<UUID>();
//            Random random = new Random();
//            for (int i=0; i < deviceCount; i++) {
//                deviceIds.add(UUID.randomUUID());
//            }
//
//            while (true) {
//                String deviceId = "device-" + (random.nextInt(5) + 1); // simulate 5 devices
//                int temperature = 15 + random.nextInt(20); // 15–35 °C
//                int humidity = 30 + random.nextInt(40);    // 30–70 %
//
//                String payload = String.format("{\"deviceId\":\"%s\",\"temperature\":%d,\"humidity\":%d}",
//                        deviceId, temperature, humidity);
//
//                client.publish("devices/" + deviceId + "/data", new MqttMessage(payload.getBytes()));
//
//                System.out.println("Published: " + payload);
//                Thread.sleep(2000); // every 2 seconds
//            }
//        }
//    }
//}
