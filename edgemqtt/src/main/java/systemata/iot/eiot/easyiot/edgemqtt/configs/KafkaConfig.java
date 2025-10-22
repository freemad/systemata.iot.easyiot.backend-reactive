package systemata.iot.eiot.easyiot.edgemqtt.configs;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Configuration
public class KafkaConfig {

    @Bean
    public SenderOptions<String, String> senderOptions(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16_384);
        return SenderOptions.create(props);
    }

    @Bean
    public KafkaSender<String, String> kafkaSender(SenderOptions<String, String> options) {
        return KafkaSender.create(options);
    }

    @Bean
    public ReceiverOptions<String, String> kafkaReceiverOptions(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap,
            @Value("${spring.application.name:eiot-app}") String appName) {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ws-cluster-consumer");
        // use unique group id per instance so each instance gets a copy of messages
        String uniqueGroupId = appName + "-" + UUID.randomUUID();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, uniqueGroupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, appName + "-" + UUID.randomUUID());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // tuning
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return ReceiverOptions.<String, String>create(props)
                // subscribe to all IoT topics (you can restrict pattern to your prefix)
                .subscription(Pattern.compile(".*")) // or Pattern.compile("^iot\\..*")
                .addAssignListener(p -> System.out.println("Partitions assigned: " + p))
                .addRevokeListener(p -> System.out.println("Partitions revoked: " + p));
    }

    @Bean
    public KafkaReceiver<String, String> kafkaReceiver(ReceiverOptions<String, String> receiverOptions) {
        return KafkaReceiver.create(receiverOptions);
    }
}