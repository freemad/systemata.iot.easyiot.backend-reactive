package systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.dtos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import systemata.iot.eiot.easyiot.common.contracts.domain.IDto;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsPublish;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsSubscribe;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsUnsubscribe;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "action")
@JsonSubTypes({
        @JsonSubTypes.Type(value = WsSubscribe.class, name = "subscribe"),
        @JsonSubTypes.Type(value = WsUnsubscribe.class, name = "unsubscribe"),
        @JsonSubTypes.Type(value = WsPublish.class, name = "publish"),
})
public interface IWsIncoming
        extends IDto {
}
