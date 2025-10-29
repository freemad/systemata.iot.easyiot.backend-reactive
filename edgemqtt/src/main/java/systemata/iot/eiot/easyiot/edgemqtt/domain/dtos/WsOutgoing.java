package systemata.iot.eiot.easyiot.edgemqtt.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.edgemqtt.domain.enums.WsMsgType;

import java.io.Serial;
import java.time.Instant;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class WsOutgoing
        implements IDto {
    @Serial
    private static final long serialVersionUID = -7093261307407098584L;
    private final String type = "ws-outgoing";
    private final String version = "1";

    private String msgType = "kafka-msg"; //WsMsgType.WS_MSG_TYPE_MQTT_MSG;
    private String topic;
    private String payload;
    private int qos;
    private boolean retained;
    private Instant receivedAt;
}
