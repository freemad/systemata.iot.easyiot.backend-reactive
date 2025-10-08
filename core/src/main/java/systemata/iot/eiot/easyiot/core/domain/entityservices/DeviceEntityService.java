package systemata.iot.eiot.easyiot.core.domain.entityservices;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.easyiot.common.contracts.services.IEntityRService;
import systemata.iot.eiot.easyiot.core.domain.daos.DeviceRepo;
import systemata.iot.eiot.easyiot.core.domain.entities.DeviceEntity;
import systemata.iot.eiot.easyiot.core.domain.models.Device;

import java.util.UUID;
import java.util.function.Function;

@Getter
@Service
@RequiredArgsConstructor
public class DeviceEntityService
        implements IEntityRService<Device, DeviceEntity, UUID> {
    private final Logger logger = LoggerFactory.getLogger(DeviceEntityService.class);

    private final DeviceRepo repository;
    private final DeviceTypeEntityService deviceTypeEntityService;
    private final AssetEntityService assetEntityService;

    public Function<DeviceEntity, Mono<Device>> getMonoModelMapper() {
        return t -> Mono.just(new Device()
                .setId(t.getId())
                .setName(t.getName())
                .setLabel(t.getLabel())
                .setHardwareId(t.getHardwareId()))
                .flatMap(m -> deviceTypeEntityService.findById(t.getDeviceTypeId())
                        .map(m::setDeviceType))
                .flatMap(m ->assetEntityService.findById(t.getAssetId())
                        .map(m::setAsset));
    }

    public Function<Device, Mono<DeviceEntity>> getMonoEntityMapper() {
        return m -> Mono.just(new DeviceEntity()
                .setId(m.getId())
                .setName(m.getName())
                .setLabel(m.getLabel())
                .setHardwareId(m.getHardwareId())
                .setAssetId(m.getAsset().getId())
                .setDeviceTypeId(m.getDeviceType().getId()));
    }
}
