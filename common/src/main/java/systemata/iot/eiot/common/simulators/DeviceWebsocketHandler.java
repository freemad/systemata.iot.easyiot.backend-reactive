//package systemata.iot.eiot.easyiot.common.simulators;
//
//import lombok.RequiredArgsConstructor;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketSession;
//import reactor.core.publisher.Mono;
//import systemata.iot.eiot.easyiot.common.configs.ApplicationProps;
//import systemata.iot.eiot.easyiot.common.exceptions.BusinessException;
//import systemata.iot.eiot.easyiot.common.exceptions.CommonErrorCode;
//import ir.shaparak.exsb.exsbcbi.edgemqtt.services.MqttService;
//
//@Component
//@RequiredArgsConstructor
//public class DeviceWebsocketHandler
//        implements WebSocketHandler {
//
//    private final MqttService mqttService;
//    private final ApplicationProps applicationProps;
//
//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
//        var mqttConfig = applicationProps.getMqttConfig();
//        try {
//            return session.send(
//                    mqttService.subscribe("devices/+/data")
//                            .map(session::textMessage)
//            );
//        } catch (MqttException e) {
//            throw new BusinessException(CommonErrorCode.DATA_TRANSFER_EXCEPTION, "websocket send to subscribed mqtt failed", e);
//        }
//    }
//}
