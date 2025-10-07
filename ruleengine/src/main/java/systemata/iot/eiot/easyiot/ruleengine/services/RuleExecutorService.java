//package services;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import lombok.RequiredArgsConstructor;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//import reactor.kafka.sender.KafkaSender;
//import reactor.kafka.sender.SenderRecord;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class RuleExecutorService {
//    private final QuickJsPool quickJsPool;
//    private final KafkaSender<String, String> kafkaSender;
//
//    // Example: in reality fetch from reactive DB
//    private List<String> fetchRulesForDevice(String deviceId) {
//        return List.of(
//                "if (telemetry.temp > 30) { emit(JSON.stringify({type:'set', path:'fan', value:'on'})); }",
//                "if (telemetry.batt < 3.3) { emit('low_batt_alarm'); }"
//        );
//    }
//
//    public Mono<Void> processTelemetry(String deviceId, JsonNode telemetry) {
//        return Mono.defer(() -> {
//            var rules = fetchRulesForDevice(deviceId);
//            return Mono.when(
//                    rules.stream()
//                            .map(script -> quickJsPool.evalRule(script, telemetry, 500)
//                                    .flatMapIterable(cmds -> cmds)
//                                    .flatMap(cmd -> {
//                                        var rec = new ProducerRecord<String,String>("device-commands", deviceId, cmd);
//                                        var srec = SenderRecord.create(rec, null);
//                                        return kafkaSender.send(Mono.just(srec)).next().then();
//                                    })
//                            ).toArray(Mono[]::new)
//            );
//        });
//    }
//}
