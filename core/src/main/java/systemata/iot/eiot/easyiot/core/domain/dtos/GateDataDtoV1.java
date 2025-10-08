package systemata.iot.eiot.easyiot.core.domain.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;

import java.io.Serial;
import java.time.Instant;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@JsonComponent
public class GateDataDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = -9033907561257509294L;
    private final String type = "gate-data";
    private final String version = "1";

    private String id;
    private GateDtoV1 gate;
    private Instant ts;
    private Double value;
}
