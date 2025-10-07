package systemata.iot.eiot.easyiot.common.contracts.domain;

import java.io.Serializable;

public interface IEntity<I extends Serializable>
        extends Serializable {
    I getId();

    IEntity<I> setId(I id);
}
