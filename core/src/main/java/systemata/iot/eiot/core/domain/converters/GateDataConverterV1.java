package systemata.iot.eiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.core.domain.dtos.GateDataDtoV1;
import systemata.iot.eiot.core.domain.models.GateData;

@Mapper(componentModel = "spring")
public interface GateDataConverterV1
        extends IModel2DtoConverter<GateData, GateDataDtoV1, String> {

}
