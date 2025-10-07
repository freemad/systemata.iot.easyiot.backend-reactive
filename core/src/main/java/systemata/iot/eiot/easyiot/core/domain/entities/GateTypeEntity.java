package systemata.iot.eiot.easyiot.core.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import systemata.iot.eiot.easyiot.common.contracts.domain.IUuidEntity;

import java.io.Serial;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "gate_type")
public class GateTypeEntity
        implements IUuidEntity {
    @Serial
    private static final long serialVersionUID = -2928044312748171459L;

    @Id
    private UUID id;
    private String name;
    private Boolean isInput;
    private Boolean isExtended;
    private Double maxValue;
    private Double baseValue;
    private Short pinCount;
}
