package systemata.iot.eiot.easyiot.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import systemata.iot.eiot.easyiot.common.contracts.domain.IShortStrEnum;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeviceEventSource
        implements IShortStrEnum {

    DEVICE_EVENT_SOURCE_UNSPECIFIED((short) 0, "unspecified"),
    DEVICE_EVENT_SOURCE_MAIN_DEVICE((short) 1, "main-device"),
    DEVICE_EVENT_SOURCE_PERIPHERAL_DEVICE((short) 2, "peripheral-device"),
    DEVICE_EVENT_SOURCE_DASHBOARD((short) 3, "dashboard"),
    DEVICE_EVENT_SOURCE_RULEENGINE((short) 4, "rule-engine"),
    DEVICE_EVENT_SOURCE_ERROR((short) 255, "error");

    private final Short value;
    private final String str;

    public static DeviceEventSource ofValue(final short value) {
        return Arrays.stream(DeviceEventSource.values())
                .filter(enm -> enm.getValue() == value)
                .findFirst().orElse(DEVICE_EVENT_SOURCE_ERROR);
    }

    public static DeviceEventSource ofStr(final String str) {
        return Arrays.stream(DeviceEventSource.values())
                .filter(enm -> enm.getStr().equals(str))
                .findFirst().orElse(DEVICE_EVENT_SOURCE_ERROR);
    }
}
