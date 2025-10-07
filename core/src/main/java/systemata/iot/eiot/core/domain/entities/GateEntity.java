package systemata.iot.eiot.core.domain.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import systemata.iot.eiot.common.contracts.domain.IUuidEntity;

import java.io.Serial;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@Table(name = "gate")
public class GateEntity
        implements IUuidEntity {
    @Serial
    private static final long serialVersionUID = 7556510476100532159L;
    private final Short gateIdx;
    private final Double baseValue;
    private final Double maxValue;

    @Id
    private UUID id;
    private String name;
    private String label;
    private Double value;
    private UUID deviceId;
    private UUID deviceTypeGateId;
}
