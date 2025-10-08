package systemata.iot.eiot.easyiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.easyiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.easyiot.core.domain.dtos.GateTypeDtoV1;
import systemata.iot.eiot.easyiot.core.domain.models.GateType;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GateTypeConverterV1
        extends IModel2DtoConverter<GateType, GateTypeDtoV1, UUID> {
}
