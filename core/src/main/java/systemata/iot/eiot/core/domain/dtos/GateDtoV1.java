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
public class GateDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = 3763706413659749634L;
    private final String type = "gate";
    private final String version = "1";

    private UUID id;
    private String name;
    private String label;
    private Double value;
    private DeviceDtoV1 device;
    private DeviceTypeGateDtoV1 deviceTypeGate;
}
