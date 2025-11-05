package systemata.iot.eiot.easyiot.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import systemata.iot.eiot.easyiot.common.contracts.domain.IShortStrEnum;
import systemata.iot.eiot.easyiot.common.domain.constants.CommonParam;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProtocolType
        implements IShortStrEnum {

    PROTOCOL_TYPE_UNSPECIFIED((short) 0, "unspecified"),
    PROTOCOL_TYPE_ONBOARD((short) 1, "onboard"),
    PROTOCOL_TYPE_WIRELESS_LORA((short) (CommonParam.PROTOCOL_TYPE_LOCAL_CONNX_INDEX + 0), "wireless-lora"),
    PROTOCOL_TYPE_WIRELESS_ZIGBEE((short) (CommonParam.PROTOCOL_TYPE_LOCAL_CONNX_INDEX + 1), "wireless-zigbee"),
    PROTOCOL_TYPE_MQTT((short) (CommonParam.PROTOCOL_TYPE_INTERNET_CONNX_INDEX + 0), "mqtt"),
    PROTOCOL_TYPE_ERROR((short) 255, "error");

    private final Short value;
    private final String str;

    public static ProtocolType ofValue(final short value) {
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
