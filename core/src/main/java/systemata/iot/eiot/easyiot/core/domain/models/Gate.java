package systemata.iot.eiot.easyiot.core.domain.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serial;
import java.util.UUID;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class Gate
        implements IEntityModel<UUID> {
    @Serial
    private static final long serialVersionUID = -8049199331927171632L;
    private final Short gateIdx;
    private final Double baseValue;
    private final Double maxValue;

    private UUID id;
    private String name;
    private String label;
    private Double value;
    private Device device;
    private DeviceTypeGate deviceTypeGate;
}
