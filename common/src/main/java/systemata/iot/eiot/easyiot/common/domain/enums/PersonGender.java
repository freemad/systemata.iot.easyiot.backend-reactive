package systemata.iot.eiot.easyiot.common.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import systemata.iot.eiot.easyiot.common.contracts.domain.IShortStrEnum;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PersonGender
        implements IShortStrEnum {

    PERSON_GENDER_UNSPECIFIED((short) 0, "unspecified"),
    PERSON_GENDER_MALE((short) 1, "male"),
    PERSON_GENDER_FEMALE((short) 2, "female"),
    PERSON_GENDER_LGBTQ((short) 3, "lgbtq"),
    PERSON_GENDER_ERROR((short) 255, "error");

    private final Short value;
    private final String str;

    public static PersonGender ofValue(final short value) {
        return Arrays.stream(PersonGender.values())
                .filter(enm -> enm.getValue() == value)
                .findFirst().orElse(PERSON_GENDER_ERROR);
    }

    public static PersonGender ofStr(final String str) {
        return Arrays.stream(PersonGender.values())
                .filter(enm -> enm.getStr().equals(str))
                .findFirst().orElse(PERSON_GENDER_ERROR);
    }
}
