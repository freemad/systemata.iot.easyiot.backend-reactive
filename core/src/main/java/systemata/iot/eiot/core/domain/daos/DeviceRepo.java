package systemata.iot.eiot.core.domain.daos;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import systemata.iot.eiot.common.contracts.services.IR2BCRepository;
import systemata.iot.eiot.core.domain.entities.DeviceEntity;

import java.util.UUID;

@EnableR2dbcRepositories(basePackageClasses = DeviceEntity.class)
public interface DeviceRepo
        extends IR2BCRepository<DeviceEntity, UUID> {
}
