package systemata.iot.eiot.easyiot.core.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PersonGender {

    PERSON_GENDER_UNSPECIFIED((byte) 0, "unspecified"),
    PERSON_GENDER_MALE((byte) 1, "male"),
    PERSON_GENDER_FEMALE((byte) 2, "female"),
    PERSON_GENDER_LGBTQ((byte) 3, "lgbtq"),
    PERSON_GENDER_ERROR((byte) 255, "error");

    private final Byte value;
    private final String str;

    public static PersonGender ofValue(final byte value) {
        return Arrays.stream(PersonGender.values())
                .filter(status -> status.getValue() == value)
                .findFirst().orElse(PERSON_GENDER_ERROR);
    }

    public static PersonGender ofStr(final String str) {
        return Arrays.stream(PersonGender.values())
                .filter(status -> status.getStr().equals(str))
                .findFirst().orElse(PERSON_GENDER_ERROR);
    }
}
