//package systemata.iot.eiot.easyiot.common.resources;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//import systemata.iot.eiot.easyiot.common.services.KafkaStreamService;
//
//@RestController
//@RequestMapping("/api/v1/telemetry")
//@RequiredArgsConstructor
//public class TelemetryController {
//
//    private final KafkaStreamService kafkaStreamService;
//
//    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<String> streamTelemetry() {
//        return kafkaStreamService.flux();
//    }
//}
