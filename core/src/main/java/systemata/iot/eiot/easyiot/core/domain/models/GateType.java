package systemata.iot.eiot.easyiot.core.domain.models;

import lombok.Data;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;
import systemata.iot.eiot.easyiot.common.domain.enums.ProtocolType;

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
    private ProtocolType protocolType;
    private Double maxValue;
    private Double baseValue;
    private Short pinCount;
}
