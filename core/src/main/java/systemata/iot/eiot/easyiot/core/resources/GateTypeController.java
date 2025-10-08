package systemata.iot.eiot.easyiot.core.resources;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systemata.iot.eiot.easyiot.common.contracts.services.ICrudController;
import systemata.iot.eiot.easyiot.core.domain.converters.GateTypeConverterV1;
import systemata.iot.eiot.easyiot.core.domain.dtos.GateTypeDtoV1;
import systemata.iot.eiot.easyiot.core.domain.entities.GateTypeEntity;
import systemata.iot.eiot.easyiot.core.domain.entityservices.GateTypeEntityService;
import systemata.iot.eiot.easyiot.core.domain.models.GateType;

import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/gateTypes")
public class GateTypeController
        implements ICrudController<GateTypeEntity, GateType, GateTypeDtoV1, UUID> {

    private final GateTypeEntityService entityService;
    private final GateTypeConverterV1 modelConverterV1;

    @Override
    public Boolean validate(GateTypeDtoV1 dto) {
        return true;
    }
}
