package systemata.iot.eiot.easyiot.core.domain.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IModel;

import java.io.Serial;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class DeviceMqttPayload
        implements IModel {
    @Serial
    private static final long serialVersionUID = -277303784836579283L;

    private UUID deviceId;
    private long timestamp;
    private List<GateMqttPayload> gates;
}
