package systemata.iot.eiot.easyiot.common.contracts.domain;

import java.io.Serializable;

public interface INumberStrEnum<N extends Serializable> {
    N getValue();

    String getStr();
}
