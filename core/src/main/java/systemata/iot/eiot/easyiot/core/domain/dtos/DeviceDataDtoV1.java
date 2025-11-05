package systemata.iot.eiot.easyiot.core.domain.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.common.domain.enums.ProtocolType;

import java.io.Serial;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@JsonComponent
public class DeviceDataDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = 2222778932968846797L;
    private final String type = "device-data";
    private final String version = "1";

    private String id;
    private Instant ts;
    private UUID deviceId;
    private Short eventType;
    private Short eventSource;
    private Set<GateDataDtoV1> gatesData;
    private ProtocolType protocolType;
    private Double value;
}
