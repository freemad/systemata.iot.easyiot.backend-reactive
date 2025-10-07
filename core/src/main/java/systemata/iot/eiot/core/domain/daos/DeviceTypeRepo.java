package systemata.iot.eiot.core.domain.daos;

import reactor.core.publisher.Mono;
import systemata.iot.eiot.common.contracts.services.IR2BCRepository;
import systemata.iot.eiot.core.domain.entities.DeviceTypeEntity;

import java.util.UUID;

public interface DeviceTypeRepo
        extends IR2BCRepository<DeviceTypeEntity, UUID> {

    public Mono<DeviceTypeEntity> findByModel(String model);
}
