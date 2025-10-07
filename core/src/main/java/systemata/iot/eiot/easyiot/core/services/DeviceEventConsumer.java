//package systemata.iot.eiot.easyiot.common.services;
//
//import org.apache.avro.generic.GenericRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//import systemata.iot.eiot.easyiot.common.utils.AvroDeserializer;
//
//@Component
//public class DeviceEventConsumer {
//    @KafkaListener(topics = "device-events", groupId = "iot-group")
//    public void listen(byte[] message) {
//        GenericRecord record = AvroDeserializer.deserialize(message);
//        System.out.println("Received Avro record: " + record);
//    }
//}
