package systemata.iot.eiot.easyiot.edgemqtt.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WsMsgType {
    WS_MSG_TYPE_MQTT_MSG((byte) 0, "mqtt-msg"),
    WS_MSG_TYPE_KAFKA_MSG((byte) 1, "kafka-msg"),
    WS_MSG_TYPE_ACK((byte) 2, "ack"),
    WS_MSG_TYPE_ERROR((byte) 3, "error");

    private final Byte value;
    private final String str;

    public static WsMsgType ofValue(final byte value) {
        return Arrays.stream(WsMsgType.values())
                .filter(status -> status.getValue() == value)
                .findFirst().orElse(WS_MSG_TYPE_ERROR);
    }

    public static WsMsgType ofStr(final String str) {
        return Arrays.stream(WsMsgType.values())
                .filter(status -> status.getStr().equals(str))
                .findFirst().orElse(WS_MSG_TYPE_ERROR);
    }
}
