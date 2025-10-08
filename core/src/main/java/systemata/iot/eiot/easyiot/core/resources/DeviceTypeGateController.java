package systemata.iot.eiot.easyiot.core.resources;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systemata.iot.eiot.easyiot.common.contracts.services.ICrudController;
import systemata.iot.eiot.easyiot.core.domain.converters.DeviceTypeGateConverterV1;
import systemata.iot.eiot.easyiot.core.domain.dtos.DeviceTypeGateDtoV1;
import systemata.iot.eiot.easyiot.core.domain.entities.DeviceTypeGateEntity;
import systemata.iot.eiot.easyiot.core.domain.entityservices.DeviceTypeGateEntityService;
import systemata.iot.eiot.easyiot.core.domain.models.DeviceTypeGate;

import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/deviceTypeGates")
public class DeviceTypeGateController
        implements ICrudController<DeviceTypeGateEntity, DeviceTypeGate, DeviceTypeGateDtoV1, UUID> {

    private final DeviceTypeGateEntityService entityService;
    private final DeviceTypeGateConverterV1 modelConverterV1;

    @Override
    public Boolean validate(DeviceTypeGateDtoV1 dto) {
        return true;
    }
}
