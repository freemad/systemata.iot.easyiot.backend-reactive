package systemata.iot.eiot.common.contracts.domain;

import java.io.Serializable;

public interface IEntityModel<I extends Serializable>
        extends Serializable {

    I getId();

    IEntityModel<I> setId(I id);
}
