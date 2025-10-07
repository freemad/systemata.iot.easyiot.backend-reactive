package systemata.iot.eiot.common.contracts.services;

import reactor.core.publisher.Mono;
import systemata.iot.eiot.common.contracts.domain.IDto;
import systemata.iot.eiot.common.contracts.domain.IEntityModel;

import java.io.Serializable;

public interface IModel2DtoMonoMapper<M extends IEntityModel<I>, D extends IDto, I extends Serializable> {
    Mono<D> toMonoDto(M model);
}
