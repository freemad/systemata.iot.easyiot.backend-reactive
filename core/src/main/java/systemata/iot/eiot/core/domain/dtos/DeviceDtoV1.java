package systemata.iot.eiot.core.domain.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;
import systemata.iot.eiot.common.contracts.domain.IDto;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@JsonComponent
public class DeviceDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = -4515771973044091546L;
    private final String type = "device-full";
    private final String version = "1";

    private UUID id;
    private String name;
    private String label;
    private UUID hardwareId;
    private AssetDtoV1 asset;
    private DeviceTypeDtoV1 deviceType;
    private List<GateDtoV1> gates;
}
