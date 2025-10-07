package systemata.iot.eiot.easyiot.core.domain.models;

import lombok.Data;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serial;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Device
        implements IEntityModel<UUID> {
    @Serial
    private static final long serialVersionUID = 1404863483164172412L;

    private UUID id;
    private String name;
    private String label;
    private UUID hardwareId;
    private Asset asset;
    private DeviceType deviceType;
}
