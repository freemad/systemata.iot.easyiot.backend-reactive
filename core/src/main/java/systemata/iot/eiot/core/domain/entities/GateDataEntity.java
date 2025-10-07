package systemata.iot.eiot.core.domain.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import systemata.iot.eiot.common.contracts.domain.IEntity;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "gate_data")
@RequiredArgsConstructor
public class GateDataEntity
        implements IEntity<String> {
    @Serial
    private static final long serialVersionUID = -360054074677671020L;

    @Id
    private String id; // generated as gateId + "_" + ts (helper in service layer)

    @Column("gate_id")
    private UUID gateId;

    @Column("ts")
    private Instant ts;

    @Column("value")
    private Double value;

    public GateDataEntity(UUID gateId, Instant ts, Double value) {
        this.gateId = gateId;
        this.ts = ts;
        this.value = value;
        this.id = gateId + "_" + ts.toEpochMilli(); // synthetic key
    }
}
