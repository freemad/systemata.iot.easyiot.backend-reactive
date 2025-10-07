package systemata.iot.eiot.core.domain.entityservices;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.common.contracts.services.IEntityRService;
import systemata.iot.eiot.core.domain.daos.GateRepo;
import systemata.iot.eiot.core.domain.entities.GateEntity;
import systemata.iot.eiot.core.domain.models.Gate;

import java.util.UUID;
import java.util.function.Function;

@Getter
@Service
@RequiredArgsConstructor
public class GateEntityService
        implements IEntityRService<Gate, GateEntity, UUID> {
    private final Logger logger = LoggerFactory.getLogger(GateEntityService.class);

    private final GateRepo repository;
    private final DeviceEntityService deviceEntityService;
    private final DeviceTypeGateEntityService deviceTypeGateEntityService;

    public Function<GateEntity, Mono<Gate>> getMonoModelMapper() {
        return t -> Mono.just(new Gate(t.getGateIdx(), t.getBaseValue(), t.getMaxValue())
                .setId(t.getId())
                .setName(t.getName())
                .setLabel(t.getLabel())
                .setValue(t.getValue()))
                .flatMap(m -> deviceEntityService.findById(t.getDeviceId())
                        .map(m::setDevice))
                .flatMap(m -> deviceTypeGateEntityService.findById(t.getDeviceTypeGateId())
                        .map(m::setDeviceTypeGate));
    }

    public Function<Gate, Mono<GateEntity>> getMonoEntityMapper() {
        return m -> Mono.just(new GateEntity(m.getGateIdx(), m.getBaseValue(), m.getMaxValue())
                .setId(m.getId())
                .setName(m.getName())
                .setLabel(m.getLabel())
                .setValue(m.getValue())
                .setDeviceId(m.getDevice().getId())
                .setDeviceTypeGateId(m.getDeviceTypeGate().getId()));
    }

    public Flux<Gate> findAllByDeviceId(UUID deviceId) {
        return repository.findAllByDeviceId(deviceId)
                .flatMap(getMonoModelMapper());
    }
}
