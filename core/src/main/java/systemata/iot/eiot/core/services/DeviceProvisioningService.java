package systemata.iot.eiot.core.services;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import systemata.iot.eiot.core.domain.entityservices.*;
import systemata.iot.eiot.easyiot.core.domain.entityservices.*;

import java.util.UUID;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class DeviceProvisioningService {

    public static final String DOUBLE_COLON_DELIMITER = " :: ";
    public static final String COLON_DELIMITER = ": ";
    private final AssetEntityService assetEntityService;
    private final DeviceEntityService deviceEntityService;
    private final DeviceTypeEntityService deviceTypeEntityService;
    private final DeviceTypeGateEntityService deviceTypeGateEntityService;
    private final GateEntityService gateEntityService;
    private final GateTypeEntityService gateTypeEntityService;

    public Mono<Device> composeDeviceAdGates(String deviceName, UUID hardwareId,
                                             DeviceType deviceType, Asset asset) {
//        var deviceId = UUID.randomUUID();
        return Mono.just(deviceType)
                .flatMap(dt -> deviceEntityService.save(new Device()
                                .setName(deviceName)
                                .setLabel(getDeviceDefaultLabel(deviceName, deviceType))
                                .setAsset(asset)
                                .setHardwareId(hardwareId)
                                .setDeviceType(deviceType)
                        )
                        .flatMap(d -> deviceTypeGateEntityService.findAllByDeviceTypeId(dt.getId())
                                    .flatMap(dtg -> gateTypeEntityService.findById(dtg.getGateType().getId())
                                            .flatMap(gt -> gateEntityService.save(new Gate(dtg.getGateIdx(), gt.getBaseValue(), gt.getMaxValue())
                                                    .setName(getGateNameAndDefaultLabel(deviceName, deviceType, dtg, gt))
                                                    .setLabel(getGateNameAndDefaultLabel(deviceName, deviceType, dtg, gt))
                                                    .setDevice(d)
                                                    .setDeviceTypeGate(dtg)
                                            )))
                                    .collectList()
                                    .map(gs -> d)));
    }

    private static String getGateNameAndDefaultLabel(String deviceName, DeviceType deviceType, DeviceTypeGate deviceTypeGate, GateType gateType) {
        return getDeviceDefaultLabel(deviceName, deviceType) +
                DOUBLE_COLON_DELIMITER + gateType.getName() + COLON_DELIMITER + deviceTypeGate.getLabel();
    }

    private static String getDeviceDefaultLabel(String deviceName, DeviceType deviceType) {
        return deviceType.getName() + COLON_DELIMITER + deviceName;
    }
}
