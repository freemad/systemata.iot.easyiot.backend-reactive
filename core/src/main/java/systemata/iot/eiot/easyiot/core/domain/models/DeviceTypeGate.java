package systemata.iot.eiot.easyiot.core.domain.models;

import lombok.Data;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serial;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class DeviceTypeGate
        implements IEntityModel<UUID> {
    @Serial
    private static final long serialVersionUID = -5648135197528111226L;

    private UUID id;
    private String label;
    private Short gateIdx;
    private DeviceType deviceType;
    private GateType gateType;
}
