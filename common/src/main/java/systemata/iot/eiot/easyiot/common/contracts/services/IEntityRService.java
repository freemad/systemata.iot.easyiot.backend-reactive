package systemata.iot.eiot.easyiot.common.contracts.services;

import org.slf4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntity;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Function;

public interface IEntityRService<
        M extends IEntityModel<I>,
        T extends IEntity<I>,
        I extends Serializable> {

    Logger getLogger();

    IR2BCRepository<T, I> getRepository();

    Function<T, Mono<M>> getMonoModelMapper();

    Function<M, Mono<T>> getMonoEntityMapper();

    default Mono<M> findById(I id) {
        return getRepository().findById(id)
                .flatMap(getMonoModelMapper());
    }

    default Flux<M> findAllById(Collection<I> ids) {
        return getRepository().findAllById(ids)
                .flatMap(getMonoModelMapper());
    }

    default Flux<M> findAllById(Flux<I> ids) {
        return getRepository().findAllById(ids)
                .flatMap(getMonoModelMapper());
    }

    default Flux<M> findAll() {
        return getRepository().findAll()
                .flatMap(getMonoModelMapper());
    }

    default Mono<M> save(M model) {
        return Mono.just(model)
                .flatMap(getMonoEntityMapper())
                .flatMap(t -> getRepository().save(t))
                .flatMap(getMonoModelMapper());
    }

    default Mono<M> save(Mono<M> eMono) {
        return eMono.flatMap(this::save);
    }

    default Mono<M> update(M model) {
        return Mono.just(model)
                .flatMap(getMonoEntityMapper())
                .flatMap(t -> getRepository().save(t))
                .flatMap(getMonoModelMapper());
    }

    default Flux<M> saveAll(Collection<M> models) {
        return Flux.fromStream(models.stream())
                .flatMap(getMonoEntityMapper())
                .flatMap(t -> getRepository().save(t))
                .flatMap(getMonoModelMapper());
    }

    default Flux<M> saveAll(Flux<M> mFlux) {
        return mFlux
                .flatMap(getMonoEntityMapper())
                .flatMap(t -> getRepository().save(t))
                .flatMap(getMonoModelMapper());
    }

    default Mono<Void> delete(M model) {
        return Mono.just(model)
                .flatMap(getMonoEntityMapper())
                .flatMap(t -> getRepository().delete(t));
    }

    default Mono<Void> deleteAll(Collection<M> models) {
        return Flux.fromStream(models.stream())
                .flatMap(getMonoEntityMapper())
                .collectList()
                .flatMap(ts -> getRepository().deleteAll(ts));
    }

    default Mono<Void> deleteAll(Flux<M> mFlux) {
        return mFlux
                .collectList()
                .flatMap(this::deleteAll);
    }

    default Mono<Void> deleteAll() {
        return getRepository().deleteAll();
    }

    default Mono<Void> deleteById(I id) {
        return getRepository().deleteById(id);
    }

    default Mono<Void> deleteAllById(Collection<I> ids) {
        return getRepository().deleteAllById(ids);
    }

    default Mono<Long> count() {
        return getRepository().count();
    }
}
