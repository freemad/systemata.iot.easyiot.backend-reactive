package systemata.iot.eiot.core.domain.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;
import systemata.iot.eiot.common.contracts.domain.IDto;

import java.io.Serial;
import java.util.UUID;

@Data
@JsonComponent
@Accessors(chain = true)
@RequiredArgsConstructor
public class ComposeDeviceDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = 6951744600362320302L;
    private final String type = "compose-device";
    private final String version = "1";

    private UUID hardwareId;
    private String deviceName;
    private String deviceTypeModel;
}
