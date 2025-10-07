package systemata.iot.eiot.easyiot.core.domain.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.jackson.JsonComponent;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.core.domain.enums.PersonGender;

import java.io.Serial;
import java.time.Instant;
import java.util.UUID;

@Data
@JsonComponent
@Accessors(chain = true)
@RequiredArgsConstructor
public class OwnerDtoV1
        implements IDto {
    @Serial
    private static final long serialVersionUID = -8034979701726055866L;
    private final String type = "owner";
    private final String version = "1";

    private UUID id;
    private String firstName;
    private String lastName;
    private Instant dateOfBirth;
    private PersonGender gender;
}
