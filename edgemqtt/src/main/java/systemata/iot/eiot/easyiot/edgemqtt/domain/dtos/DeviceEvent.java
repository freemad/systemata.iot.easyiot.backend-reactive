package systemata.iot.eiot.easyiot.edgemqtt.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.common.domain.enums.DeviceEventType;
import systemata.iot.eiot.easyiot.common.domain.enums.DeviceEventSource;

import java.io.Serial;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class DeviceEvent
        implements IDto {
    @Serial
    private static final long serialVersionUID = -7093261307407098584L;
    private final String type = "device-event";
    private final String version = "1";

    private UUID deviceId;
    private Instant ts;
    private DeviceEventType eventType;
    private DeviceEventSource eventSource;
    private Double data;
    private Map<String, Object> metadata;
}
