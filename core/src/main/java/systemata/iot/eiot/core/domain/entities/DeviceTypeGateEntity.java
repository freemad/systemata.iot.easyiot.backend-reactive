package systemata.iot.eiot.core.domain.entities;

import lombok.Getter;
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
@Table(name = "device_type_gate")
public class DeviceTypeGateEntity
        implements IUuidEntity {
    @Serial
    private static final long serialVersionUID = 886514034101054103L;

    @Id
    private UUID id;
    private String label;
    private Short gateIdx;
    private UUID deviceTypeId;
    private UUID gateTypeId;
}
