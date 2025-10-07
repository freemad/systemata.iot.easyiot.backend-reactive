package systemata.iot.eiot.easyiot.core.resources;

import systemata.iot.eiot.easyiot.core.domain.entityservices.AssetEntityService;
import systemata.iot.eiot.easyiot.core.domain.entityservices.DeviceEntityService;
import systemata.iot.eiot.easyiot.core.domain.entityservices.DeviceTypeEntityService;
import systemata.iot.eiot.easyiot.core.domain.entityservices.OwnerEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.domain.IUuidEntity;
import systemata.iot.eiot.easyiot.core.domain.converters.Device2DeviceAndGatesMonoMapperV1;
import systemata.iot.eiot.easyiot.core.domain.dtos.ComposeDeviceDtoV1;
import systemata.iot.eiot.easyiot.core.domain.dtos.DeviceAndGatesDtoV1;
import systemata.iot.eiot.easyiot.core.domain.models.Asset;
import systemata.iot.eiot.easyiot.core.services.DeviceProvisioningService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/devices-and-gates")
public class DeviceAndGatesController {

    private final DeviceProvisioningService deviceProvisioningService;
    private final DeviceTypeEntityService deviceTypeEntityService;
    private final AssetEntityService assetEntityService;
    private final OwnerEntityService ownerEntityService;
    private final DeviceEntityService deviceEntityService;
    private final Device2DeviceAndGatesMonoMapperV1 device2DeviceAndGatesMonoMapperV1;

    @GetMapping("")
    public Flux<DeviceAndGatesDtoV1> listDevices() {
        return deviceEntityService.findAll()
                .flatMap(device2DeviceAndGatesMonoMapperV1::toMonoDto);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<DeviceAndGatesDtoV1> createDevice(@RequestBody ComposeDeviceDtoV1 dto) {
        var ownerMono = ownerEntityService.findById(IUuidEntity.getDefaultUuid());
        var assetMono = ownerMono.flatMap(o -> assetEntityService.save(new Asset()
                .setName(dto.getDeviceName())
                .setLabel("Asset: " + dto.getDeviceName())
                        .setOwner(o)));
        var deviceTypeMono = deviceTypeEntityService.findByModel(dto.getDeviceTypeModel());

        return deviceTypeMono
                .flatMap(dt -> assetMono.flatMap(
                        a -> deviceProvisioningService.composeDeviceAdGates(dto.getDeviceName(), dto.getHardwareId(), dt, a)
                ))
                .flatMap(device2DeviceAndGatesMonoMapperV1::toMonoDto);
    }

}
