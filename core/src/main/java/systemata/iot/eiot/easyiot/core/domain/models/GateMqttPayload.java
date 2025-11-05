package systemata.iot.eiot.easyiot.core.domain.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IModel;
import systemata.iot.eiot.easyiot.common.domain.enums.ProtocolType;

import java.io.Serial;
import java.util.UUID;

@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class GateMqttPayload
        implements IModel {
    @Serial
    private static final long serialVersionUID = -1742424067501609880L;

    private UUID gateId;
    private String gateLabel;
    private ProtocolType protocolType;
    private Double data;
    private long timestamp;
}
