//package systemata.iot.eiot.easyiot.common.base.contracts.services;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.TopicPartition;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.slf4j.Logger;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.boot.ssl.DefaultSslBundleRegistry;
//import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public interface IKafkaConsumer {
//
//    Logger getLogger();
//
//    KafkaProperties getKafkaProps();
//
//    default Map<TopicPartition, Long> getLastProcessedOffset() {
//        return new ConcurrentHashMap<>();
//    }
//
//    default ReactiveKafkaConsumerTemplate<String, String> getReactiveKafkaConsumerTemplate() {
//        return new ReactiveKafkaConsumerTemplate<>();
//    }
//
//    default Map<String, Object> getReceiverProps() {
//        Map<String, Object> props = getKafkaProps().buildConsumerProperties(new DefaultSslBundleRegistry());
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", getKafkaProps().getBootstrapServers()));
////        props.put(ConsumerConfig.CLIENT_ID_CONFIG, getAdapterAttribs().getName());
////        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaUtils.consumerRequestGroupId());
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // We will commit manually
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
//        return props;
//    }
//}
