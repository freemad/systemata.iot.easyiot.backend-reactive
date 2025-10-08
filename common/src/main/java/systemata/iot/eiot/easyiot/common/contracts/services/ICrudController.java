package systemata.iot.eiot.easyiot.common.contracts.services;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntity;
import systemata.iot.eiot.easyiot.common.contracts.domain.IEntityModel;
import systemata.iot.eiot.easyiot.common.exceptions.BusinessException;
import systemata.iot.eiot.easyiot.common.exceptions.CommonErrorCode;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public interface ICrudController<
        T extends IEntity<I>,
        M extends IEntityModel<I>,
        D extends IDto,
        I extends Serializable> {

    IEntityRService<M, T, I> getEntityService();

    IModel2DtoConverter<M, D, I> getModelConverterV1();

    Boolean validate(D dto);

    @GetMapping("")
    default Flux<D> all() {
        return getEntityService().findAll()
                .map(getModelConverterV1()::toDto);
    }

    @GetMapping("/{id}")
    default Mono<D> byId(@PathVariable("id") I id) {
        return getEntityService().findById(id)
                .map(getModelConverterV1()::toDto);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    default Mono<D> create(@RequestBody D dto) {
        return Mono.just(dto)
                .filter(this::validate)
                .map(d -> getModelConverterV1().toModel(d))
                .flatMap(getEntityService()::save)
                .map(getModelConverterV1()::toDto)
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "validation error")));
    }

    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    default Mono<List<D>> bulk(@RequestBody Collection<D> dtos) {
        return Mono.just(dtos)
                .map(ds -> {
                    var allValid = ds.stream().allMatch(this::validate);
                    return allValid ? ds : Collections.emptyList();
                })
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "validation error")))
                .then(Mono.fromCallable(() -> dtos.stream().map(d -> getModelConverterV1().toModel(d)).toList()))
                .flatMap(ms -> getEntityService().saveAll(ms).collectList())
                .map(ms -> ms.stream().map(getModelConverterV1()::toDto).toList())
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.DATA_PERSISTENCE_EXCEPTION, "error in persisting data")));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    default Mono<D> update(@PathVariable I id, @RequestBody D dto) {
        return Mono.just(dto)
                .filter(this::validate)
                .flatMap(d -> getEntityService().findById(id)
                        .filter(Objects::nonNull)
                        .flatMap(om -> getModelConverterV1()
                                .toModelMono(d)
                                .flatMap(nm -> getEntityService().save((M) nm.setId(om.getId())))
                        )
                        .map(getModelConverterV1()::toDto)
                        .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "validation error")))
                )
                .switchIfEmpty(Mono.error(new BusinessException(CommonErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "validation error")));
    }

    @DeleteMapping("/{id}")
    default Mono<Void> delete(@PathVariable("id") I id) {
        return getEntityService().deleteById(id);
    }
}
