package systemata.iot.eiot.easyiot.common.contracts.services;

import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serializable;

public interface IModel2DtoMapper<M extends IEntityModel<I>, D extends IDto, I extends Serializable> {
    D toDto(M model);

    default Mono<D> toDtoMono(M model) {
        return Mono.just(toDto(model));
    }
}
