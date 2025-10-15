package systemata.iot.eiot.easyiot.edgemqtt.domain.dtos;

import lombok.Data;
import systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.dtos.IWsIncoming;

import java.io.Serial;

@Data
public class WsSubscribe
        implements IWsIncoming {
    @Serial
    private static final long serialVersionUID = 1825604082279167981L;
    private final String type = "ws-subscribe";
    private final String version = "1";

    private String topic;
}
