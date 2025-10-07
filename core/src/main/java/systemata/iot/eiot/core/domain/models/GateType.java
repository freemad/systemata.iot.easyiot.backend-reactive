package systemata.iot.eiot.core.domain.models;

import lombok.Data;
import lombok.experimental.Accessors;
import systemata.iot.eiot.common.contracts.domain.IEntityModel;

import java.io.Serial;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class GateType
        implements IEntityModel<UUID> {
    @Serial
    private static final long serialVersionUID = -3341277913257384971L;

    private UUID id;
    private String name;
    private Boolean isInput;
    private Boolean isExtended;
    private Double maxValue;
    private Double baseValue;
    private Short pinCount;
}
