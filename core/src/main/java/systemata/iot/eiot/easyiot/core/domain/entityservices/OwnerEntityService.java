package systemata.iot.eiot.easyiot.core.domain.entityservices;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.services.IEntityRService;
import systemata.iot.eiot.easyiot.core.domain.daos.OwnerRepo;
import systemata.iot.eiot.easyiot.core.domain.entities.OwnerEntity;
import systemata.iot.eiot.easyiot.core.domain.enums.PersonGender;
import systemata.iot.eiot.easyiot.core.domain.models.Owner;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Getter
@Service
@RequiredArgsConstructor
public class OwnerEntityService
        implements IEntityRService<Owner, OwnerEntity, UUID> {
    private final Logger logger = LoggerFactory.getLogger(OwnerEntityService.class);

    private final OwnerRepo repository;

    public Function<OwnerEntity, Mono<Owner>> getMonoModelMapper() {
        return t -> Mono.just(new Owner()
                .setId(t.getId())
                .setFirstName(t.getFirstName())
                .setLastName(t.getLastName())
                .setDateOfBirth(new Date(t.getDateOfBirth()))
                .setGender(PersonGender.ofValue(t.getGender())));
    }

    public Function<Owner, Mono<OwnerEntity>> getMonoEntityMapper() {
        return m -> Mono.just(new OwnerEntity()
                .setId(m.getId())
                .setFirstName(m.getFirstName())
                .setLastName(m.getLastName())
                .setDateOfBirth(m.getDateOfBirth().getTime())
                .setGender(m.getGender().getValue()));
    }
}
