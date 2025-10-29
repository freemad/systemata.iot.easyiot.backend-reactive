//package systemata.iot.eiot.easyiot.edgemqtt.services;
//
//// File: src/main/java/com/example/iot/websocket/MqttWebSocketHandler.java
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketMessage;
//import org.springframework.web.reactive.socket.WebSocketSession;
//import reactor.core.Disposable;
//import reactor.core.Disposables;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.publisher.Sinks;
//import reactor.core.scheduler.Schedulers;
//import systemata.iot.eiot.easyiot.common.domain.models.MqttMessageEnvelope;
//import systemata.iot.eiot.easyiot.common.exceptions.BusinessException;
//import systemata.iot.eiot.easyiot.common.exceptions.CommonErrorCode;
//import systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.dtos.IWsIncoming;
//import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsOutgoing;
//import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsPublish;
//import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsSubscribe;
//import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsUnsubscribe;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class MqttWebSocketHandlerOld
//        implements WebSocketHandler {
//
//    private final ObjectMapper objectMapper;
//    private final MqttService mqttService;
//
//    // track per-session subscriptions so we can dispose on close/unsubscribe
//    private final Map<String, Disposable> sessionSubscriptions = new ConcurrentHashMap<>();
//    // composite disposable to cleanup all on close
//    private final Disposable.Composite composite = Disposables.composite();
//    // dynamic outgoing sink for this session
//    private final Sinks.Many<String> outgoingSink = Sinks.many().multicast().onBackpressureBuffer();
//
//    // incoming messages from the client are JSON; we'll deserialize to WsIncoming
//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
//        // outboundFlux: messages from all MQTT subscriptions for this session
////        Flux<WebSocketMessage> outboundFlux = getOutboundFlux(session, sessionSubscriptions, composite); // ensure outboundFlux remains (we'll actually produce messages differently)
//
//        /*
//         * The approach above started per-topic Flux subscriptions but didn't connect them into `session.send`.
//         *
//         * Better approach: rather than subscribing inside the receive flatMap, we:
//         * - create a merged outgoing Flux from per-topic sinks that are added on subscribe,
//         * - then session.send(outgoingFlux) will push messages to client.
//         *
//         * Let's instead create a dynamic emitter subject for this session and route MQTT events into it.
//         */
//
//        // Rework: when a subscribe arrives, add a mapping to forward to outgoingSink (not subscribe directly)
//        // So we must reimplement receive handling to add/remove forwarders.
//
//        // Clean approach: use `session.receive()` to process control messages, and maintain map of forwarders (Disposable)
//        Mono<Void> control = session.receive()
//                .map(WebSocketMessage::getPayloadAsText)
//                .flatMap(this::handlePayload)
//                .then();
//
//        // outbound Flux from outgoingSink as WebSocketMessage
//        // cleanup when client disconnects or cancels
//        Flux<WebSocketMessage> outbound = outgoingSink.asFlux()
//                .map(session::textMessage)
//                .doOnCancel(this::cleanUpGracefully);
//
//        // combine control handling and outbound send
//        return Mono.when(control, session.send(outbound)).then()
//                .doFinally(sig -> {
//                    // ensure cleanup
//                    cleanUpGracefully();
//                });
////        return Mono.zip(control, session.send(outbound)).then()
////                .doFinally(sig -> {
////                    // ensure cleanup
////                    cleanUpGracefully();
////                });
//    }
//
//    private Mono<Void> handlePayload(String payload) {
//        try {
//            IWsIncoming incoming = objectMapper.readValue(payload, IWsIncoming.class);
//            switch (incoming) {
//                case WsSubscribe s -> {
//                    return doSubscribe(s);
//                }
//                case WsUnsubscribe u -> {
//                    return doUnsubscribe(u);
//                }
//                case WsPublish p -> {
//                    return doPublish(p);
//                }
//                case null, default -> {
//                    return Mono.empty();
//                }
//            }
//        } catch (MqttException e) {
//            log.error("failed to publish to mqtt service: {}", e.getMessage());
//            throw new BusinessException(CommonErrorCode.DATA_TRANSFER_EXCEPTION, "failed to publish to mqtt service", e);
//        } catch (Exception e) {
//            log.error("failed to parse ws incoming: {}", e.getMessage());
//            throw new BusinessException(CommonErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, "failed to parse ws incoming object", e);
//        }
//    }
//
//    private void cleanUpGracefully() {
//        composite.dispose();
//        sessionSubscriptions.values().forEach(Disposable::dispose);
//        sessionSubscriptions.clear();
//    }
//
//    private Mono<Void> doPublish(WsPublish publish) {
//        return mqttService.publish(publish.getTopic(), publish.getPayload(),
//                publish.getQos(), publish.isRetained()).then();
//    }
//
//    private Mono<Void> doUnsubscribe(WsUnsubscribe unsubscribe) {
//        String topic = unsubscribe.getTopic();
//        Disposable d = sessionSubscriptions.remove(topic);
//        if (d != null) d.dispose();
//        return Mono.empty();
//    }
//
//    private Mono<Void> doSubscribe(WsSubscribe subscribe) throws MqttException {
//        String topic = subscribe.getTopic();
//        if (sessionSubscriptions.containsKey(topic)) {
//            return Mono.empty();
//        }
//        // create forward: mqttService.subscribe(topic) -> map to WS json -> emit into outgoingSink
//        Disposable forward = subscribeToMqttTopic(topic, outgoingSink);
//        sessionSubscriptions.put(topic, forward);
//        composite.add(forward);
//        return Mono.empty();
//    }
//
//    private Disposable subscribeToMqttTopic(String topic, Sinks.Many<String> outgoingSink) throws MqttException {
//        return mqttService.subscribe(topic)
//                .map(this::toWsOutgoing)
//                .map(this::toJsonText)
//                .subscribe(outgoingSink::tryEmitNext);
//    }
//
//    private Flux<WebSocketMessage> getOutboundFlux(WebSocketSession session, Map<String, Disposable> sessionSubscriptions, Disposable.Composite composite) {
//        return session.receive()
//                .publishOn(Schedulers.boundedElastic())
//                // handle incoming client messages
//                .flatMap(webSocketMessage -> {
//                    String payload = webSocketMessage.getPayloadAsText();
//                    try {
//                        IWsIncoming incoming = objectMapper.readValue(payload, IWsIncoming.class);
//
//                        switch (incoming) {
//                            case WsSubscribe subscribe ->
//                                    wsSubscribeOld(session, subscribe, sessionSubscriptions, composite);
//                            case WsUnsubscribe unsubscribe -> wsUnsubscribeOld(session, unsubscribe, sessionSubscriptions);
//                            case WsPublish publish -> wsPublishOld(session, publish);
//                            case null, default -> {
//                                log.warn("unknown incoming type: {}", incoming);
//                                return Mono.empty();
//                            }
//                        }
//                    } catch (Exception ex) {
//                        log.error("failed to process incoming ws message: {}", ex.getMessage());
//                        return Mono.empty();
//                    }
//                    return Mono.empty();
//                })
//                // We used direct subscription above; we still need a heartbeat to keep session.send working.
//                .thenMany(Flux.never());
//    }
//
//    private Mono<Object> wsPublishOld(WebSocketSession session, WsPublish publish) {
//        log.info("WS {} publish to {} payload {}", session.getId(), publish.getTopic(), publish.getPayload());
//        return mqttService.publish(publish.getTopic(), publish.getPayload(), publish.getQos(), publish.isRetained())
//                .then(Mono.empty());
//    }
//
//    private static Mono<Object> wsUnsubscribeOld(WebSocketSession session, WsUnsubscribe unsubscribe,
//                                                 Map<String, Disposable> sessionSubscriptions) {
//        String topic = unsubscribe.getTopic();
//        log.info("WS {} unsubscribe {} ", session.getId(), topic);
//        Disposable d = sessionSubscriptions.remove(topic);
//        if (d != null) d.dispose();
//        return Mono.empty();
//    }
//
//    private Mono<Object> wsSubscribeOld(WebSocketSession session, WsSubscribe subscribe,
//                                        Map<String, Disposable> sessionSubscriptions,
//                                        Disposable.Composite composite) throws MqttException {
//        String topic = subscribe.getTopic();
//        log.info("ws {} subscribe {} ", session.getId(), topic);
//
//        // If already subscribed, ignore
//        if (!sessionSubscriptions.containsKey(topic)) {
//            Flux<WebSocketMessage> msgs = mqttService.subscribe(topic)
//                    .map(this::toWsOutgoing)
//                    .map(this::toJsonText)
//                    .map(session::textMessage)
//                    .onErrorResume(e -> {
//                        log.error("subscribe error for {}: {}", topic, e.getMessage());
//                        return Mono.empty();
//                    });
//
//            // Subscribe to the stream and keep track (we subscribe but we also return nothing here)
//            Disposable d = msgs.publishOn(Schedulers.boundedElastic()).subscribe(); // start pushing directly to client via session.send below
//            sessionSubscriptions.put(topic, d);
//            composite.add(d);
//        }
//        return Mono.empty();
//    }
//
//    private WsOutgoing toWsOutgoing(MqttMessageEnvelope env) {
//        return new WsOutgoing("mqtt-message", env.getTopic(), env.getPayload(), env.getQos(), env.getRetained(), env.getReceivedAt());
//    }
//
//    @SneakyThrows
//    private String toJsonText(Object obj) {
//        return objectMapper.writeValueAsString(obj);
//    }
//}
