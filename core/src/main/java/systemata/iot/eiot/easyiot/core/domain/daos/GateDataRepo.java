package systemata.iot.eiot.easyiot.core.domain.daos;

import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.services.IR2BCRepository;
import systemata.iot.eiot.easyiot.core.domain.entities.GateDataEntity;

import java.time.Instant;
import java.util.UUID;

public interface GateDataRepo
        extends IR2BCRepository<GateDataEntity, String> {
    @Query("SELECT * FROM gate_data WHERE gate_id = :gateId AND ts BETWEEN :from AND :to ORDER BY ts ASC")
    Flux<GateDataEntity> findAllByGateIdAndTsBetween(UUID gateId, Instant from, Instant to);

    @Query("SELECT * FROM gate_data WHERE gate_id = :gateId AND ts >= :from ORDER BY ts ASC")
    Flux<GateDataEntity> findAllByGateIdAndTsFrom(UUID gateId, Instant from);

    @Query("SELECT * FROM gate_data WHERE gate_id = :gateId AND ts <= :to ORDER BY ts ASC")
    Flux<GateDataEntity> findAllByGateIdAndTsTo(UUID gateId, Instant to);

    @Query("SELECT * FROM gate_data WHERE gate_id = :gateId ORDER BY ts DESC LIMIT 1")
    Mono<GateDataEntity> findLatestByGateId(UUID gateId);
}
