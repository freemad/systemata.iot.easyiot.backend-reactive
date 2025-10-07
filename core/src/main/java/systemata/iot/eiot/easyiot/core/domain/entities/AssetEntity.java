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
@Table(name = "asset")
public class AssetEntity
        implements IUuidEntity {
    @Serial
    private static final long serialVersionUID = -7968237967185809089L;

    @Id
    private UUID id;
    private String name;
    private String label;
    private UUID ownerId;
}
