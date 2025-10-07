package systemata.iot.eiot.easyiot.common.contracts.domain;

import java.io.Serializable;

public interface IVersioned
        extends Serializable {
    String getVersion();
}
