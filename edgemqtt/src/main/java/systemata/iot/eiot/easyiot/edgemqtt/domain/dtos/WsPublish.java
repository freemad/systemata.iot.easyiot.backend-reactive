package systemata.iot.eiot.easyiot.edgemqtt.domain.dtos;

import lombok.Data;
import systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.dtos.IWsIncoming;

import java.io.Serial;

@Data
public class WsPublish
        implements IWsIncoming {
    @Serial
    private static final long serialVersionUID = -7969172049907728507L;
    private final String type = "ws-publish";
    private final String version = "1";

    private String topic;
    private String payload;
    private int qos = 1;
    private boolean retained = false;
}
