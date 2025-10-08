package systemata.iot.eiot.easyiot.core.resources;

import systemata.iot.eiot.easyiot.core.domain.entityservices.GateEntityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import systemata.iot.eiot.easyiot.common.contracts.services.ICrudController;
import systemata.iot.eiot.easyiot.core.domain.converters.GateConverterV1;
import systemata.iot.eiot.easyiot.core.domain.dtos.GateDtoV1;
import systemata.iot.eiot.easyiot.core.domain.entities.GateEntity;
import systemata.iot.eiot.easyiot.core.domain.models.Gate;

import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/gates")
public class GateController
        implements ICrudController<GateEntity, Gate, GateDtoV1, UUID> {

    private final GateEntityService entityService;
    private final GateConverterV1 modelConverterV1;

    @Override
    public Boolean validate(GateDtoV1 dto) {
        return true;
    }
}
