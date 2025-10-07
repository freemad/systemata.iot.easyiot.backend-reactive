package systemata.iot.eiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.core.domain.dtos.DeviceDtoV1;
import systemata.iot.eiot.core.domain.models.Device;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DeviceConverterV1
        extends IModel2DtoConverter<Device, DeviceDtoV1, UUID> {

}
