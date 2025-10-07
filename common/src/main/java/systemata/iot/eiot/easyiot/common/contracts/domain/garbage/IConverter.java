package systemata.iot.eiot.easyiot.common.contracts.domain.garbage;

import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serializable;

public interface IConverter<M extends IEntityModel<I>, D extends IDto, I extends Serializable> {
    D toDto(M model);

    M toModel(D dto);
}
