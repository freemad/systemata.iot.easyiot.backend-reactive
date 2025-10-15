package systemata.iot.eiot.easyiot.edgemqtt.resources;

import lombok.extern.slf4j.Slf4j;
import systemata.iot.eiot.easyiot.common.domain.models.MqttMessageEnvelope;
import systemata.iot.eiot.easyiot.edgemqtt.configs.MqttProps;
import systemata.iot.eiot.easyiot.edgemqtt.services.MqttService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.exceptions.BusinessException;
import systemata.iot.eiot.easyiot.common.exceptions.CommonErrorCode;

@Slf4j
@RestController
@RequestMapping("/api/v1/mqtt")
@RequiredArgsConstructor
public class MqttController {

    private final MqttService mqttService;
    private final MqttProps mqttProps;

    // Stream MQTT as Server-Sent Events
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MqttMessageEnvelope> stream(@RequestParam String topic) {
        log.info("subscribing into topic: {}", topic);
        return mqttService.stream(topic);
    }

    @PostMapping("/publish")
    public Mono<Void> publish(
            @RequestParam String topic,
            @RequestParam(defaultValue = "1") int qos,
            @RequestParam(defaultValue = "false") boolean retained,
            @RequestBody String payload) {
        log.info("just publishing message: {}, into topic: {}", payload, topic);
        return mqttService.publish(topic, payload, qos, retained)
                .onErrorResume( e -> Mono.error(new BusinessException(CommonErrorCode.DATA_TRANSFER_EXCEPTION, "mqtt service publish failed", e)));
    }
}
