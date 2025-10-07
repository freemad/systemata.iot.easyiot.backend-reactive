package systemata.iot.eiot.core.domain.entityservices;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.common.contracts.services.IEntityRService;
import systemata.iot.eiot.core.domain.daos.GateDataRepo;
import systemata.iot.eiot.core.domain.entities.GateDataEntity;
import systemata.iot.eiot.core.domain.models.GateData;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

@Getter
@Service
@RequiredArgsConstructor
public class GateDataEntityService
        implements IEntityRService<GateData, GateDataEntity, String> {
    private final Logger logger = LoggerFactory.getLogger(GateDataEntityService.class);

    private final GateDataRepo repository;
    private final GateEntityService gateEntityService;

    public Function<GateDataEntity, Mono<GateData>> getMonoModelMapper() {
        return t -> Mono.just(new GateData()
                .setId(t.getId())
                .setTs(t.getTs())
                .setValue(t.getValue()))
                .flatMap(m -> gateEntityService.findById(t.getGateId())
                        .map(m::setGate));
    }

    public Function<GateData, Mono<GateDataEntity>> getMonoEntityMapper() {
        return m -> Mono.just(new GateDataEntity()
                .setId(m.getId())
                .setTs(m.getTs())
                .setGateId(m.getGate().getId())
                .setValue(m.getValue()));
    }

    public Flux<GateData> findAllByGateIdAndTsBetween(UUID gateId, Instant from, Instant to) {
        return repository.findAllByGateIdAndTsBetween(gateId, from, to)
                .flatMap(getMonoModelMapper());
    }

    public Flux<GateData> findAllByGateIdAndTsFrom(UUID gateId, Instant from) {
        return repository.findAllByGateIdAndTsFrom(gateId, from)
                .flatMap(getMonoModelMapper());
    }

    public Flux<GateData> findAllByGateIdAndTsTo(UUID gateId, Instant to) {
        return repository.findAllByGateIdAndTsTo(gateId, to)
                .flatMap(getMonoModelMapper());
    }

    public Mono<GateData> findLatestByGateId(UUID gateId) {
        return repository.findLatestByGateId(gateId)
                .flatMap(getMonoModelMapper());
    }
}
