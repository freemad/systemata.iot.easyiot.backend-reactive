package systemata.iot.eiot.common.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import systemata.iot.eiot.common.contracts.domain.IModel;

import java.io.Serial;
import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MqttMessageEnvelope
        implements IModel {
    @Serial
    private static final long serialVersionUID = -756450343360839241L;

    private String topic;
    private int qos;
    private String payload;
    private Boolean retained;
    private Instant receivedAt;
    private Map<String, String> metadata;
}
