package systemata.iot.eiot.easyiot.core.domain.models;

import lombok.Data;
import lombok.experimental.Accessors;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;
import systemata.iot.eiot.easyiot.core.domain.enums.PersonGender;

import java.io.Serial;
import java.util.Date;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Owner
        implements IEntityModel<UUID> {
    @Serial
    private static final long serialVersionUID = -6447133528159145818L;

    private UUID id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private PersonGender gender;
}
