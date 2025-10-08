package systemata.iot.eiot.easyiot.core.resources;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import systemata.iot.eiot.easyiot.common.contracts.services.ICrudController;
import systemata.iot.eiot.easyiot.core.domain.converters.AssetConverterV1;
import systemata.iot.eiot.easyiot.core.domain.dtos.AssetDtoV1;
import systemata.iot.eiot.easyiot.core.domain.entities.AssetEntity;
import systemata.iot.eiot.easyiot.core.domain.entityservices.AssetEntityService;
import systemata.iot.eiot.easyiot.core.domain.models.Asset;

import java.util.UUID;

@Slf4j
@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/assets")
public class AssetController 
        implements ICrudController<AssetEntity, Asset, AssetDtoV1, UUID> {

    private final AssetEntityService entityService;
    private final AssetConverterV1 modelConverterV1;

    @Override
    public Boolean validate(AssetDtoV1 dto) {
        return true;
    }
}
