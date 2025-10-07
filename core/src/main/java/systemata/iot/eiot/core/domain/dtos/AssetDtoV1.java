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
public class AssetDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = -1754989804907338353L;
    private final String type = "asset";
    private final String version = "1";

    private UUID id;
    private String name;
    private String label;
    private OwnerDtoV1 owner;
}
