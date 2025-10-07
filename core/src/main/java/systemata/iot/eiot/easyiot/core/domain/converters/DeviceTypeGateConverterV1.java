package systemata.iot.eiot.easyiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.easyiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.easyiot.core.domain.dtos.DeviceTypeGateDtoV1;
import systemata.iot.eiot.easyiot.core.domain.models.DeviceTypeGate;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DeviceTypeGateConverterV1
        extends IModel2DtoConverter<DeviceTypeGate, DeviceTypeGateDtoV1, UUID> {

}
