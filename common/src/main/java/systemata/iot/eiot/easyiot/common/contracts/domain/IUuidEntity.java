package systemata.iot.eiot.easyiot.common.contracts.domain;

import java.util.UUID;

public interface IUuidEntity
        extends IEntity<UUID> {

    static UUID getDefaultUuid() {
        return UUID.fromString("12345678-1234-1234-1234-123456789abc");
    }
}
