package systemata.iot.eiot.easyiot.core.resources;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systemata.iot.eiot.easyiot.common.contracts.services.ICrudController;
import systemata.iot.eiot.easyiot.core.domain.converters.DeviceTypeConverterV1;
import systemata.iot.eiot.easyiot.core.domain.dtos.DeviceTypeDtoV1;
import systemata.iot.eiot.easyiot.core.domain.entities.DeviceTypeEntity;
import systemata.iot.eiot.easyiot.core.domain.entityservices.DeviceTypeEntityService;
import systemata.iot.eiot.easyiot.core.domain.models.DeviceType;

import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/deviceTypes")
public class DeviceTypeController
        implements ICrudController<DeviceTypeEntity, DeviceType, DeviceTypeDtoV1, UUID> {

    private final DeviceTypeEntityService entityService;
    private final DeviceTypeConverterV1 modelConverterV1;

    @Override
    public Boolean validate(DeviceTypeDtoV1 dto) {
        return true;
    }
}
