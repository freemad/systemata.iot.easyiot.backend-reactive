package systemata.iot.eiot.easyiot.common.contracts.services;

import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serializable;

public interface IDto2ModelMapper<D extends IDto, M extends IEntityModel<I>, I extends Serializable> {
    M toModel(D dto);

    default Mono<M> toModelMono(D dto) {
        return Mono.just(toModel(dto));
    }
}
