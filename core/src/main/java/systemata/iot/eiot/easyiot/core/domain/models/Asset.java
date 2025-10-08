package systemata.iot.eiot.easyiot.core.domain.models;

import lombok.Data;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serial;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Asset
        implements IEntityModel<UUID> {
    @Serial
    private static final long serialVersionUID = 2545633955345626362L;

    private UUID id;
    private String name;
    private String label;
    private Owner owner;
}
