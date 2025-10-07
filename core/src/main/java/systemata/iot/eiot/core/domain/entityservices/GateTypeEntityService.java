package systemata.iot.eiot.core.domain.entityservices;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.common.contracts.services.IEntityRService;
import systemata.iot.eiot.core.domain.daos.GateTypeRepo;
import systemata.iot.eiot.core.domain.entities.GateTypeEntity;
import systemata.iot.eiot.core.domain.models.GateType;

import java.util.UUID;
import java.util.function.Function;

@Getter
@Service
@RequiredArgsConstructor
public class GateTypeEntityService
        implements IEntityRService<GateType, GateTypeEntity, UUID> {
    private final Logger logger = LoggerFactory.getLogger(GateTypeEntityService.class);

    private final GateTypeRepo repository;

    public Function<GateTypeEntity, Mono<GateType>> getMonoModelMapper() {
        return t -> Mono.just(new GateType()
                .setId(t.getId())
                .setName(t.getName())
                .setIsInput(t.getIsInput())
                .setIsExtended(t.getIsExtended())
                .setMaxValue(t.getMaxValue())
                .setBaseValue(t.getBaseValue())
                .setPinCount(t.getPinCount()));
    }

    public Function<GateType, Mono<GateTypeEntity>> getMonoEntityMapper() {
        return m -> Mono.just(new GateTypeEntity()
                .setId(m.getId())
                .setName(m.getName())
                .setIsInput(m.getIsInput())
                .setIsExtended(m.getIsExtended())
                .setMaxValue(m.getMaxValue())
                .setBaseValue(m.getBaseValue())
                .setPinCount(m.getPinCount()));
    }
}
