package systemata.iot.eiot.easyiot.core.domain.converters;

import org.mapstruct.Mapper;
import systemata.iot.eiot.easyiot.common.contracts.services.IModel2DtoConverter;
import systemata.iot.eiot.easyiot.core.domain.dtos.AssetDtoV1;
import systemata.iot.eiot.easyiot.core.domain.models.Asset;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AssetConverterV1
        extends IModel2DtoConverter<Asset, AssetDtoV1, UUID> {

}
