package systemata.iot.eiot.core.domain.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;
import systemata.iot.eiot.common.contracts.domain.IDto;

import java.io.Serial;
import java.util.List;

@Data
@JsonComponent
@Accessors(chain = true)
@RequiredArgsConstructor
public class DeviceAndGatesDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = 4772991701425011542L;
    private final String type = "device-and-gates";
    private final String version = "1";

    private DeviceDtoV1 deviceDtoV1;
    private List<GateDtoV1> gateDtoV1s;
}
