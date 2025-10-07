package systemata.iot.eiot.core.resources;

import systemata.iot.eiot.core.domain.entityservices.GateEntityService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import systemata.iot.eiot.common.contracts.services.ICrudController;
import systemata.iot.eiot.core.domain.converters.GateConverterV1;
import systemata.iot.eiot.core.domain.dtos.GateDtoV1;
import systemata.iot.eiot.core.domain.entities.GateEntity;
import systemata.iot.eiot.core.domain.models.Gate;

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
