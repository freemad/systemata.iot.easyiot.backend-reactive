package systemata.iot.eiot.easyiot.core.domain.daos;

import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.services.IR2BCRepository;
import systemata.iot.eiot.easyiot.core.domain.entities.DeviceDataEntity;

import java.time.Instant;
import java.util.UUID;

public interface GateDataRepo
        extends IR2BCRepository<DeviceDataEntity, String> {
    @Query("SELECT * FROM gate_data WHERE gate_id = :gateId AND ts BETWEEN :from AND :to ORDER BY ts ASC")
    Flux<DeviceDataEntity> findAllByGateIdAndTsBetween(UUID gateId, Instant from, Instant to);

    @Query("SELECT * FROM gate_data WHERE gate_id = :gateId AND ts >= :from ORDER BY ts ASC")
    Flux<DeviceDataEntity> findAllByGateIdAndTsFrom(UUID gateId, Instant from);

    @Query("SELECT * FROM gate_data WHERE gate_id = :gateId AND ts <= :to ORDER BY ts ASC")
    Flux<DeviceDataEntity> findAllByGateIdAndTsTo(UUID gateId, Instant to);

    @Query("SELECT * FROM gate_data WHERE gate_id = :gateId ORDER BY ts DESC LIMIT 1")
    Mono<DeviceDataEntity> findLatestByGateId(UUID gateId);
}
