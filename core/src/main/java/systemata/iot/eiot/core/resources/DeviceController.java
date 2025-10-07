package systemata.iot.eiot.core.resources;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systemata.iot.eiot.common.contracts.services.ICrudController;
import systemata.iot.eiot.core.domain.converters.DeviceConverterV1;
import systemata.iot.eiot.core.domain.dtos.DeviceDtoV1;
import systemata.iot.eiot.core.domain.entities.DeviceEntity;
import systemata.iot.eiot.core.domain.entityservices.DeviceEntityService;
import systemata.iot.eiot.core.domain.models.Device;

import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController
        implements ICrudController<DeviceEntity, Device, DeviceDtoV1, UUID> {

    private final DeviceEntityService entityService;
    private final DeviceConverterV1 modelConverterV1;

    @Override
    public Boolean validate(DeviceDtoV1 dto) {
        return true;
    }
}
