//package resources;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//import services.RuleExecutorService;
//
//@RestController
//@RequiredArgsConstructor
//public class TelemetryController {
//    private final RuleExecutorService executor;
//
//    @PostMapping("/telemetry/{deviceId}")
//    public Mono<ResponseEntity<Void>> telemetry(@PathVariable String deviceId, @RequestBody JsonNode telemetry) {
//        return executor.processTelemetry(deviceId, telemetry)
//                .thenReturn(ResponseEntity.accepted().<Void>build());
//    }
//}