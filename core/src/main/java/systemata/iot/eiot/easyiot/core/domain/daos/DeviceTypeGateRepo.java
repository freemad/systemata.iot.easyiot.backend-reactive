package systemata.iot.eiot.easyiot.core.domain.daos;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.core.publisher.Flux;
import systemata.iot.eiot.easyiot.common.contracts.services.IR2BCRepository;
import systemata.iot.eiot.easyiot.core.domain.entities.DeviceTypeGateEntity;

import java.util.UUID;

@EnableR2dbcRepositories(basePackageClasses = DeviceTypeGateEntity.class)
public interface DeviceTypeGateRepo
        extends IR2BCRepository<DeviceTypeGateEntity, UUID> {

    Flux<DeviceTypeGateEntity> findAllByDeviceTypeId(UUID deviceTypeId);
}
