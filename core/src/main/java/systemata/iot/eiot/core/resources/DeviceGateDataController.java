package systemata.iot.eiot.core.resources;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import systemata.iot.eiot.core.domain.converters.GateDataConverterV1;
import systemata.iot.eiot.core.domain.dtos.GateDataDtoV1;
import systemata.iot.eiot.core.domain.entityservices.GateDataEntityService;
import systemata.iot.eiot.core.domain.entityservices.GateEntityService;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/device-gate-data")
public class DeviceGateDataController {

    private final GateEntityService gateEntityService;
    private final GateDataEntityService gateDataEntityService;
    private final GateDataConverterV1 gateDataConverterV1;

    @GetMapping("/{id}/latest")
    public Flux<GateDataDtoV1> latest(@PathVariable UUID id) {
        return gateEntityService.findAllByDeviceId(id)
                .concatMap(g -> gateDataEntityService.findLatestByGateId(g.getId()))
                .map(gateDataConverterV1::toDto);
    }

    @GetMapping("/{id}/between")
    public Flux<GateDataDtoV1> between(
            @PathVariable UUID id,
            @RequestParam("from") Instant from,
            @RequestParam("to") Instant to) {
        return gateEntityService.findAllByDeviceId(id)
                .concatMap(g -> gateDataEntityService.findAllByGateIdAndTsBetween(g.getId(), from, to))
                .map(gateDataConverterV1::toDto);
    }

    @GetMapping("/{id}/from")
    public Flux<GateDataDtoV1> from(
            @PathVariable UUID id,
            @RequestParam("from") Instant from) {
        return gateEntityService.findAllByDeviceId(id)
                .concatMap(g -> gateDataEntityService.findAllByGateIdAndTsFrom(g.getId(), from))
                .map(gateDataConverterV1::toDto);
    }

    @GetMapping("/{id}/to")
    public Flux<GateDataDtoV1> to(
            @PathVariable UUID id,
            @RequestParam("to") Instant to) {
        return gateEntityService.findAllByDeviceId(id)
                .concatMap(g -> gateDataEntityService.findAllByGateIdAndTsTo(g.getId(), to))
                .map(gateDataConverterV1::toDto);
    }
}
