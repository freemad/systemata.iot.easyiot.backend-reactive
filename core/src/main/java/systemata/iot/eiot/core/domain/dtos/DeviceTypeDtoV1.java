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
public class DeviceTypeDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = 6700773709577163955L;
    private final String type = "device-type";
    private final String version = "1";

    private UUID id;
    private String name;
    private String model;
    private String description;
}
