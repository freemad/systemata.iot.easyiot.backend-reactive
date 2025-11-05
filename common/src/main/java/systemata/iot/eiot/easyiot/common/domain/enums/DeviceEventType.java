package systemata.iot.eiot.easyiot.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import systemata.iot.eiot.easyiot.common.contracts.domain.IShortStrEnum;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeviceEventType
        implements IShortStrEnum {

    DEVICE_EVENT_TYPE_UNSPECIFIED((short) 0, "unspecified"),
    DEVICE_EVENT_TYPE_TELEMETRY((short) 1, "telemetry"),
    DEVICE_EVENT_TYPE_CONTROL((short) 2, "control"),
    DEVICE_EVENT_TYPE_COMMAND((short) 3, "command"),
    DEVICE_EVENT_TYPE_ERROR((short) 255, "error");

    private final Short value;
    private final String str;

    public static DeviceEventType ofValue(final short value) {
        return Arrays.stream(DeviceEventType.values())
                .filter(enm -> enm.getValue() == value)
                .findFirst().orElse(DEVICE_EVENT_TYPE_ERROR);
    }

    public static DeviceEventType ofStr(final String str) {
        return Arrays.stream(DeviceEventType.values())
                .filter(enm -> enm.getStr().equals(str))
                .findFirst().orElse(DEVICE_EVENT_TYPE_ERROR);
    }
}
