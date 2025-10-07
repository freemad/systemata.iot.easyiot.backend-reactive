package systemata.iot.eiot.easyiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.easyiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.easyiot.core.domain.dtos.DeviceTypeDtoV1;
import systemata.iot.eiot.easyiot.core.domain.models.DeviceType;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DeviceTypeConverterV1
        extends IModel2DtoConverter<DeviceType, DeviceTypeDtoV1, UUID> {

}
