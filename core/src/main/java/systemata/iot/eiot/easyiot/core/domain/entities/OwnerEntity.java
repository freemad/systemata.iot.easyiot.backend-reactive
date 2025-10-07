package systemata.iot.eiot.easyiot.core.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import systemata.iot.eiot.easyiot.common.contracts.domain.IUuidEntity;

import java.io.Serial;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "owner")
public class OwnerEntity
        implements IUuidEntity {
    @Serial
    private static final long serialVersionUID = 3559096304111319849L;

    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private Long dateOfBirth;
    private Byte gender;
}
