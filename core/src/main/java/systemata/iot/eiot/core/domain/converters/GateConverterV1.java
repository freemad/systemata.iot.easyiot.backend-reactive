package systemata.iot.eiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.core.domain.dtos.GateDtoV1;
import systemata.iot.eiot.core.domain.models.Gate;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GateConverterV1
        extends IModel2DtoConverter<Gate, GateDtoV1, UUID> {

}
