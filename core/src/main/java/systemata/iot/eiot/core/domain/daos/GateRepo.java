package systemata.iot.eiot.core.domain.daos;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.core.publisher.Flux;
import systemata.iot.eiot.common.contracts.services.IR2BCRepository;
import systemata.iot.eiot.core.domain.entities.GateEntity;

import java.util.UUID;

@EnableR2dbcRepositories(basePackageClasses = GateEntity.class)
public interface GateRepo
        extends IR2BCRepository<GateEntity, UUID> {

    Flux<GateEntity> findAllByDeviceId(UUID deviceId);
}
