//package systemata.iot.eiot.easyiot.edgemqtt.services.garbage;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PreDestroy;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.WebSocketMessage;
//import org.springframework.web.reactive.socket.WebSocketSession;
//import reactor.core.Disposable;
//import reactor.core.Disposables;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.dtos.IWsIncoming;
//import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsOutgoing;
//import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsPublish;
//import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsSubscribe;
//import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsUnsubscribe;
//import systemata.iot.eiot.easyiot.edgemqtt.services.KafkaClusterSubscriptionService;
//import systemata.iot.eiot.easyiot.edgemqtt.services.MqttService;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class MqttWebSocketHandler
//        implements WebSocketHandler {
//
//    private final ObjectMapper objectMapper;
//    private final MqttService mqttService;
//    private final KafkaClusterSubscriptionService clusterSubscriptionService;
//
//    // Per WebSocket session
//    private final Map<String, Map<String, Flux<WsOutgoing>>> sessionTopicFluxes = new ConcurrentHashMap<>();
//    private final Map<String, Disposable.Composite> sessionDisposables = new ConcurrentHashMap<>();
//
//    @Override
//    public Mono<Void> handle(WebSocketSession session) {
//        String sessionId = session.getId();
//        sessionTopicFluxes.put(sessionId, new ConcurrentHashMap<>());
//        sessionDisposables.put(sessionId, Disposables.composite());
//
//        log.info("new webSocket session [{}] opened", sessionId);
//
//        // incoming messages (subscribe, unsubscribe, publish)
//        Mono<Void> incoming = session.receive()
//                .map(WebSocketMessage::getPayloadAsText)
//                .flatMap(payload -> handleIncoming(payload, session))
//                .onErrorContinue((e, o) -> log.error("Error handling incoming WS: {}", e.getMessage()))
//                .then();
//
//        // outgoing stream: merge all topic fluxes for this session
//        Flux<WebSocketMessage> outbound = Flux.defer(() ->
//                Flux.merge(sessionTopicFluxes.get(sessionId).values())
//                        .map(this::serializeToText)
//                        .map(session::textMessage)
//        );
////        // for high-throughput
////        Flux<WebSocketMessage> outbound = Flux.combineLatest(
////                sessionTopicFluxes.get(sessionId).values(),
////                results -> session.textMessage(serializeToText(results[0]))
////        );
//
//        return session.send(outbound)
//                .and(incoming)
//                .doFinally(sig -> cleanUp(sessionId))
//                .then();
//    }
//
//    private Mono<Void> handleIncoming(String payload, WebSocketSession session) {
//        try {
//            IWsIncoming incoming = objectMapper.readValue(payload, IWsIncoming.class);
//            return switch (incoming) {
//                case WsSubscribe s -> handleSubscribe(s, session);
//                case WsUnsubscribe u -> handleUnsubscribe(u, session);
//                case WsPublish p -> mqttService.publish(p.getTopic(), p.getPayload(), p.getQos(), p.isRetained());
//                default -> Mono.empty();
//            };
//        } catch (Exception e) {
//            log.error("failed to parse incoming ws payload: {}", e.getMessage(), e);
//            return Mono.empty();
//        }
//    }
//
//    private Mono<Void> handleSubscribe(WsSubscribe s, WebSocketSession session) {
//        String topic = s.getTopic();
//        String sessionId = session.getId();
//
//        Map<String, Flux<WsOutgoing>> topicFluxes = sessionTopicFluxes.get(sessionId);
//        if (topicFluxes.containsKey(topic)) {
//            log.debug("session [{}] already subscribed to [{}]", sessionId, topic);
//            return Mono.empty();
//        }
//
//        Flux<WsOutgoing> flux = clusterSubscriptionService.subscribe(topic)
//                .doOnSubscribe(sub -> log.info("session [{}] subscribed to topic [{}]", sessionId, topic))
//                .doOnCancel(() -> log.info("session [{}] unsubscribed from topic [{}]", sessionId, topic))
//                .doOnError(e -> log.error("error in topic [{}] stream: {}", topic, e.getMessage()));
//
//        topicFluxes.put(topic, flux);
//        return Mono.empty();
//    }
//
//    private Mono<Void> handleUnsubscribe(WsUnsubscribe u, WebSocketSession session) {
//        String sessionId = session.getId();
//        Map<String, Flux<WsOutgoing>> topicFluxes = sessionTopicFluxes.get(sessionId);
//        topicFluxes.remove(u.getTopic());
//        log.info("session [{}] unsubscribed from [{}]", sessionId, u.getTopic());
//        return Mono.empty();
//    }
//
//    private void cleanUp(String sessionId) {
//        log.info("cleaning up websocket session [{}]", sessionId);
//        sessionTopicFluxes.remove(sessionId);
//        Disposable.Composite composite = sessionDisposables.remove(sessionId);
//        if (composite != null) composite.dispose();
//    }
//
//    @SneakyThrows
//    private String serializeToText(WsOutgoing out) {
//        return objectMapper.writeValueAsString(out);
//    }
//
//    @PreDestroy
//    public void shutdown() {
//        log.info("shutting down all webSocket sessions...");
//        sessionDisposables.values().forEach(Disposable.Composite::dispose);
//        sessionDisposables.clear();
//        sessionTopicFluxes.clear();
//    }
//}