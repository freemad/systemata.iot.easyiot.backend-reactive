package systemata.iot.eiot.easyiot.edgemqtt.domain.dtos;

import lombok.Data;
import systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.dtos.IWsIncoming;

import java.io.Serial;

@Data
public class WsUnsubscribe
        implements IWsIncoming {
    @Serial
    private static final long serialVersionUID = -5580903841366469809L;
    private final String type = "ws-unsubscribe";
    private final String version = "1";

    private String topic;
}
