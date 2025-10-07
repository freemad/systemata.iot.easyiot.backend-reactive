package systemata.iot.eiot.common.contracts.domain.garbage;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.common.contracts.domain.IEntity;
import systemata.iot.eiot.common.contracts.domain.IEntityModel;
import systemata.iot.eiot.common.contracts.services.IEntityRService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public interface IEntityRController<M extends IEntityModel<I>, T extends IEntity<I>, I extends Serializable> {

    IEntityRService<M, T, I> getEntityRService();

    @GetMapping("")
    default Flux<M> all() {
        return this.getEntityRService().findAll();
    }

    @GetMapping("/{id}")
    default Mono<M> byId(@PathVariable("id") I id) {
        return this.getEntityRService().findById(id);
    }

    @PostMapping("/")
    default Mono<M> create(@RequestBody M model) {
        return this.getEntityRService().save(model);
    }

    @PostMapping("/bulk")
    default Flux<M> createBulk(@RequestBody Collection<M> models) {
        return this.getEntityRService().saveAll(models);
    }

    @PutMapping("/{id}")
    default Mono<M> update(@PathVariable("id") I id,
                           @RequestBody M model) {
        return this.getEntityRService().findById(id)
                .filter(Objects::nonNull)
                .flatMap(t -> {
                    model.setId(t.getId());
                    return this.getEntityRService().save(model);
                })
                .switchIfEmpty(Mono.error(new IllegalArgumentException("id is not valid")));

    }

    @DeleteMapping("/{id}")
    default Mono<Void> delete(@PathVariable("id") I id) {
        return this.getEntityRService().deleteById(id);
    }
}
