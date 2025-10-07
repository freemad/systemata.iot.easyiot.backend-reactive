package systemata.iot.eiot.core.domain.entityservices;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.common.contracts.services.IEntityRService;
import systemata.iot.eiot.core.domain.daos.DeviceTypeRepo;
import systemata.iot.eiot.core.domain.entities.DeviceTypeEntity;
import systemata.iot.eiot.core.domain.models.DeviceType;

import java.util.UUID;
import java.util.function.Function;

@Getter
@Service
@RequiredArgsConstructor
public class DeviceTypeEntityService
        implements IEntityRService<DeviceType, DeviceTypeEntity, UUID> {
    private final Logger logger = LoggerFactory.getLogger(DeviceTypeEntityService.class);

    private final DeviceTypeRepo repository;

    public Function<DeviceTypeEntity, Mono<DeviceType>> getMonoModelMapper() {
        return t -> Mono.just(new DeviceType()
                .setId(t.getId())
                .setName(t.getName())
                .setDescription(t.getDescription()));
    }

    public Function<DeviceType, Mono<DeviceTypeEntity>> getMonoEntityMapper() {
        return m -> Mono.just(new DeviceTypeEntity()
                .setId(m.getId())
                .setName(m.getName())
                .setDescription(m.getDescription()));
    }

    public Mono<DeviceType> findByModel(String model) {
        return repository.findByModel(model)
                .flatMap(getMonoModelMapper());
    }
}
