package systemata.iot.eiot.easyiot.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class KafkaStreamService {

//    private final ApplicationProps applicationProps;
//    private final Integer bufferSize = 10_000;
//    private final Boolean autoCancel = false;
//    private final Integer streamTimeoutDurationInMin = 30;
//
//    private final Map<String, Sinks.Many<String>> topicSinks = new ConcurrentHashMap<>();
//    private final Set<String> subscribedTopics = ConcurrentHashMap.newKeySet();

    private final Sinks.Many<String> sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(10_000, false);

    @KafkaListener(topics = {"iot.telemetry", "iot.command", "iot.control"}, groupId = "dashboard-stream")
    public void onMessage(String value) {
        sink.tryEmitNext(value);
    }

    public Flux<String> flux() {
        return sink.asFlux();
    }
}
