package systemata.iot.eiot.easyiot.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum DeviceGateType {

    DEVICE_GATE_TYPE_UNSPECIFIED((short) 0, false, (short) 0, "unspecified"),
    DEVICE_GATE_TYPE_DIGITAL_BINARY_INPUT((short) 1, true, (short) 2, "digital-binary-input"),
    DEVICE_GATE_TYPE_DIGITAL_BINARY_OutPUT((short) 2, false, (short) 2, "digital-binary-output"),
    DEVICE_GATE_TYPE_ERROR((short) 255, false, (short) 0, "error");

    private final Short value;
    private final Boolean isInput;
    private final Short stateCount;
    private final String str;

    public Byte pinCount() {
        var requiredPins = Math.sqrt(this.stateCount);
        var pins = (int) Math.abs(requiredPins);
        return (requiredPins - Math.abs(requiredPins) > 0) ? (byte) (pins + 1) : (byte) pins;
    }

    public static DeviceGateType ofValue(final short value) {
        return Arrays.stream(DeviceGateType.values())
                .filter(status -> status.getValue() == value)
                .findFirst().orElse(DEVICE_GATE_TYPE_ERROR);
    }

    public static DeviceGateType ofStr(final String str) {
        return Arrays.stream(DeviceGateType.values())
                .filter(status -> status.getStr().equals(str))
                .findFirst().orElse(DEVICE_GATE_TYPE_ERROR);
    }

    public static List<DeviceGateType> ofStateCount(final short stateCount) {
        return Arrays.stream(DeviceGateType.values())
                .filter(status -> status.getStateCount().equals(stateCount))
                .toList();
    }
}
