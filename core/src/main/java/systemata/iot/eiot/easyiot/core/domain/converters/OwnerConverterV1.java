package systemata.iot.eiot.easyiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.easyiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.easyiot.core.domain.dtos.OwnerDtoV1;
import systemata.iot.eiot.easyiot.core.domain.models.Owner;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OwnerConverterV1
        extends IModel2DtoConverter<Owner, OwnerDtoV1, UUID> {
}
