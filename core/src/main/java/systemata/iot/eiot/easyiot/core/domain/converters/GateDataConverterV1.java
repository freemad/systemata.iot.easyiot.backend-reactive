package systemata.iot.eiot.easyiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.easyiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.easyiot.core.domain.dtos.GateDataDtoV1;
import systemata.iot.eiot.easyiot.core.domain.models.GateData;

@Mapper(componentModel = "spring")
public interface GateDataConverterV1
        extends IModel2DtoConverter<GateData, GateDataDtoV1, String> {

}
