package systemata.iot.eiot.common.contracts.domain;

import java.io.Serializable;

public interface IVersioned
        extends Serializable {
    String getVersion();
}
