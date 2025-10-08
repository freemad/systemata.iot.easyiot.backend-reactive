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
@Table(name = "device")
public class DeviceEntity
        implements IUuidEntity {
    @Serial
    private static final long serialVersionUID = -773977436088773588L;

    @Id
    private UUID id;
    private String name;
    private String label;
    private UUID hardwareId;
    private UUID assetId;
    private UUID deviceTypeId;
}
