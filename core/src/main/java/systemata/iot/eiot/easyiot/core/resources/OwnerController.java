package systemata.iot.eiot.easyiot.core.resources;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systemata.iot.eiot.easyiot.common.contracts.services.ICrudController;
import systemata.iot.eiot.easyiot.core.domain.converters.OwnerConverterV1;
import systemata.iot.eiot.easyiot.core.domain.dtos.OwnerDtoV1;
import systemata.iot.eiot.easyiot.core.domain.entities.OwnerEntity;
import systemata.iot.eiot.easyiot.core.domain.entityservices.OwnerEntityService;
import systemata.iot.eiot.easyiot.core.domain.models.Owner;

import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/owners")
public class OwnerController
        implements ICrudController<OwnerEntity, Owner, OwnerDtoV1, UUID> {

    private final OwnerEntityService entityService;
    private final OwnerConverterV1 modelConverterV1;

    @Override
    public Boolean validate(OwnerDtoV1 dto) {
        return true;
    }
}
