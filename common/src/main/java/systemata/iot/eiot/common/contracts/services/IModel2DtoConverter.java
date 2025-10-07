package systemata.iot.eiot.common.contracts.services;


import systemata.iot.eiot.common.contracts.domain.IDto;
import systemata.iot.eiot.common.contracts.domain.IEntityModel;

import java.io.Serializable;

public interface IModel2DtoConverter<M extends IEntityModel<I>, D extends IDto, I extends Serializable>
        extends IModel2DtoMapper<M, D, I>, IDto2ModelMapper<D, M, I> {
}
