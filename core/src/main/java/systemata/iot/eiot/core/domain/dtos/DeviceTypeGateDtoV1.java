package systemata.iot.eiot.core.domain.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;
import systemata.iot.eiot.common.contracts.domain.IDto;

import java.io.Serial;
import java.util.UUID;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@JsonComponent
public class DeviceTypeGateDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = 3763706413659749634L;
    private final String type = "device-type-gate";
    private final String version = "1";

    private UUID id;
    private String label;
    private Short gateIdx;
    private DeviceTypeDtoV1 deviceType;
    private GateTypeDtoV1 gateType;
}
