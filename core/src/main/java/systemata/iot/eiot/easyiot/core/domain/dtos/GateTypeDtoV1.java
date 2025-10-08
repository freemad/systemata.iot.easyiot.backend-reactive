package systemata.iot.eiot.easyiot.core.domain.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;

import java.io.Serial;
import java.util.UUID;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@JsonComponent
public class GateTypeDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = 3833534118898101449L;
    private final String type = "gate-type";
    private final String version = "1";

    private UUID id;
    private String name;
    private Boolean isInput;
    private Boolean isExtended;
    private Double maxValue;
    private Double baseValue;
    private Short pinCount;
}
