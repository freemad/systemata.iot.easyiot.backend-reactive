package systemata.iot.eiot.easyiot.edgemqtt.resources;

import systemata.iot.eiot.easyiot.common.domain.models.MqttMessageEnvelope;
import systemata.iot.eiot.easyiot.edgemqtt.services.MqttService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.exceptions.BusinessException;
import systemata.iot.eiot.easyiot.common.exceptions.CommonErrorCode;

@RestController
@RequestMapping("/api/v1/mqtt")
@RequiredArgsConstructor
public class MqttController {

    private final MqttService mqttService;

    // Stream MQTT as Server-Sent Events
    @GetMapping(value = "/{topic}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MqttMessageEnvelope> stream(@PathVariable("topic") String topic) {
        return mqttService.stream(topic);
    }

    @PostMapping("/{topic}/publish")
    public Mono<Void> publish(
            @PathVariable("topic") String topic,
            @RequestParam(defaultValue = "1") int qos,
            @RequestParam(defaultValue = "false") boolean retained,
            @RequestBody String payload
            ) {
        return Mono.fromRunnable(() -> {
            try {
                mqttService.publish(topic, payload, qos, retained);
            }
            catch (Exception e) {
                throw new BusinessException(CommonErrorCode.DATA_TRANSFER_EXCEPTION, "mqtt service publish failed", e);
            }
        });
    }
}
