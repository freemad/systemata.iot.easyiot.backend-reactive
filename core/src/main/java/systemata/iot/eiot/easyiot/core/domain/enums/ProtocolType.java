package systemata.iot.eiot.easyiot.core.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import systemata.iot.eiot.easyiot.common.contracts.domain.IByteStrEnum;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProtocolType
        implements IByteStrEnum {

    PROTOCOL_TYPE_UNSPECIFIED((byte) 0, "unspecified"),
    PROTOCOL_TYPE_DIRECT((byte) 1, "direct"),
    PROTOCOL_TYPE_WIRELESS_LORA((byte) 2, "wireless-lora"),
    PROTOCOL_TYPE_ERROR((byte) 127, "error");

    private final Byte value;
    private final String str;

    public static ProtocolType ofValue(final byte value) {
        return Arrays.stream(ProtocolType.values())
                .filter(enm -> enm.getValue() == value)
                .findFirst().orElse(PROTOCOL_TYPE_ERROR);
    }

    public static ProtocolType ofStr(final String str) {
        return Arrays.stream(ProtocolType.values())
                .filter(enm -> enm.getStr().equals(str))
                .findFirst().orElse(PROTOCOL_TYPE_ERROR);
    }
}
