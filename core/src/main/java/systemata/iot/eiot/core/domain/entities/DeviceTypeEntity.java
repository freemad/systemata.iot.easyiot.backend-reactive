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
@Table(name = "device_type")
public class DeviceTypeEntity
        implements IUuidEntity {
    @Serial
    private static final long serialVersionUID = -5235527591806637190L;

    @Id
    private UUID id;
    private String name;
    private String model;
    private String description;
}
