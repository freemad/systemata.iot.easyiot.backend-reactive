package systemata.iot.eiot.easyiot.core.domain.daos;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import systemata.iot.eiot.easyiot.common.contracts.services.IR2BCRepository;
import systemata.iot.eiot.easyiot.core.domain.entities.AssetEntity;

import java.util.UUID;

@EnableR2dbcRepositories(basePackageClasses = AssetEntity.class)
public interface AssetRepo
        extends IR2BCRepository<AssetEntity, UUID> {
}
