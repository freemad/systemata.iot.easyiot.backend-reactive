package systemata.iot.eiot.core.domain.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.common.contracts.services.IModel2DtoMonoMapper;
import systemata.iot.eiot.core.domain.dtos.DeviceAndGatesDtoV1;
import systemata.iot.eiot.easyiot.common.domain.entityservices.GateEntityService;
import systemata.iot.eiot.core.domain.models.Device;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Device2DeviceAndGatesMonoMapperV1
        implements IModel2DtoMonoMapper<Device, DeviceAndGatesDtoV1, UUID> {

    private final GateEntityService gateEntityService;
    private final DeviceConverterV1 deviceConverterV1;
    private final GateConverterV1 gateConverterV1;

    @Override
    public Mono<DeviceAndGatesDtoV1> toMonoDto(Device model) {
        return gateEntityService.findAllByDeviceId(model.getId()).collectList()
                .map(gs -> gs.stream().map(gateConverterV1::toDto).toList())
                .map(gds -> new DeviceAndGatesDtoV1()
                        .setDeviceDtoV1(deviceConverterV1.toDto(model))
                        .setGateDtoV1s(gds));
    }
}
