package systemata.iot.eiot.easyiot.core.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import systemata.iot.eiot.easyiot.common.contracts.domain.IByteStrEnum;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DeviceDataType
        implements IByteStrEnum {

    DEVICE_DATA_TYPE_UNSPECIFIED((byte) 0, "unspecified"),
    DEVICE_DATA_TYPE_TELEMETRY((byte) 1, "telemetry"),
    DEVICE_DATA_TYPE_CONTROL((byte) 2, "control"),
    DEVICE_DATA_TYPE_COMMAND((byte) 3, "command"),
    DEVICE_DATA_TYPE_ERROR((byte) 255, "error");

    private final Byte value;
    private final String str;

    public static DeviceDataType ofValue(final byte value) {
        return Arrays.stream(DeviceDataType.values())
                .filter(enm -> enm.getValue() == value)
                .findFirst().orElse(DEVICE_DATA_TYPE_ERROR);
    }

    public static DeviceDataType ofStr(final String str) {
        return Arrays.stream(DeviceDataType.values())
                .filter(enm -> enm.getStr().equals(str))
                .findFirst().orElse(DEVICE_DATA_TYPE_ERROR);
    }
}
