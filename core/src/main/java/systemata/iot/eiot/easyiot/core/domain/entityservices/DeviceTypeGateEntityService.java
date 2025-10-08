package systemata.iot.eiot.easyiot.core.domain.entityservices;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.services.IEntityRService;
import systemata.iot.eiot.easyiot.core.domain.daos.DeviceTypeGateRepo;
import systemata.iot.eiot.easyiot.core.domain.entities.DeviceTypeGateEntity;
import systemata.iot.eiot.easyiot.core.domain.models.DeviceTypeGate;

import java.util.UUID;
import java.util.function.Function;

@Getter
@Service
@RequiredArgsConstructor
public class DeviceTypeGateEntityService
        implements IEntityRService<DeviceTypeGate, DeviceTypeGateEntity, UUID> {
    private final Logger logger = LoggerFactory.getLogger(DeviceTypeGateEntityService.class);

    private final DeviceTypeGateRepo repository;
    private final GateTypeEntityService gateTypeEntityService;
    private final DeviceTypeEntityService deviceTypeEntityService;

    public Function<DeviceTypeGateEntity, Mono<DeviceTypeGate>> getMonoModelMapper() {
        return t -> Mono.just(new DeviceTypeGate()
                .setId(t.getId())
                .setLabel(t.getLabel())
                .setGateIdx(t.getGateIdx()))
                .flatMap(m -> gateTypeEntityService.findById(t.getGateTypeId())
                        .map(m::setGateType))
                .flatMap(m -> deviceTypeEntityService.findById(t.getDeviceTypeId())
                        .map(m::setDeviceType));

    }

    public Function<DeviceTypeGate, Mono<DeviceTypeGateEntity>> getMonoEntityMapper() {
        return m -> Mono.just(new DeviceTypeGateEntity()
                .setId(m.getId())
                .setLabel(m.getLabel())
                .setGateIdx(m.getGateIdx())
                .setGateTypeId(m.getGateType().getId())
                .setDeviceTypeId(m.getDeviceType().getId()));
    }

    public Flux<DeviceTypeGate> findAllByDeviceTypeId(UUID deviceTypeId) {
        return repository.findAllByDeviceTypeId(deviceTypeId)
                .flatMap(getMonoModelMapper());
    }
}
