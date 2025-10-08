package systemata.iot.eiot.easyiot.core.domain.entityservices;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.services.IEntityRService;
import systemata.iot.eiot.easyiot.core.domain.daos.AssetRepo;
import systemata.iot.eiot.easyiot.core.domain.entities.AssetEntity;
import systemata.iot.eiot.easyiot.core.domain.models.Asset;

import java.util.UUID;
import java.util.function.Function;

@Getter
@Service
@RequiredArgsConstructor
public class AssetEntityService
        implements IEntityRService<Asset, AssetEntity, UUID> {
    private final Logger logger = LoggerFactory.getLogger(AssetEntityService.class);

    private final AssetRepo repository;
    private final OwnerEntityService ownerEntityService;

    public Function<AssetEntity, Mono<Asset>> getMonoModelMapper() {
        return t -> Mono.just(new Asset()
                                .setId(t.getId())
                                .setName(t.getName())
                                .setLabel(t.getLabel())
//                .setOwner(t.getOwnerId())
                )
                .flatMap(m -> ownerEntityService.findById(t.getOwnerId())
                        .map(m::setOwner));
    }

    public Function<Asset, Mono<AssetEntity>> getMonoEntityMapper() {
        return m -> Mono.just(new AssetEntity()
                .setId(m.getId())
                .setName(m.getName())
                .setLabel(m.getLabel())
                .setOwnerId(m.getOwner().getId())
        );
    }
}
