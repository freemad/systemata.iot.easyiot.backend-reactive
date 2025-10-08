package systemata.iot.eiot.easyiot.common.contracts.domain.garbage;

import systemata.iot.eiot.easyiot.common.contracts.domain.IEntity;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serializable;
import java.util.Optional;

public interface IEntity2ModelMapper<T extends IEntity<I>, M extends IEntityModel, I extends Serializable> {
    M toModel(T entity);

    default Optional<M> toModel(Optional<T> opEntity) {
        return Optional.of(toModel(opEntity.orElse(null)));
    }
}
