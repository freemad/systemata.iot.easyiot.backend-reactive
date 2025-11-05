package systemata.iot.eiot.easyiot.edgemqtt.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.dtos.IWsIncoming;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.DeviceEvent;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsPublish;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsSubscribe;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsUnsubscribe;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaWebSocketHandler
        implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final KafkaClusterSubscriptionService kafkaClusterService;

    // Each WebSocket session holds its own topic subscriptions
    private final Map<String, Disposable> sessionSubscriptions = new ConcurrentHashMap<>();
    // Shared outgoing sink per connection (messages from Kafka -> WS)
    private final Sinks.Many<String> outgoingSink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        log.info("websocket session {} connected", session.getId());

        // Handle messages coming *from* client
        Mono<Void> inbound = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(payload -> handleIncoming(session, payload))
                .doOnError(e -> log.error("websocket inbound error: {}", e.getMessage()))
                .doFinally(sig -> cleanupSession(session))
                .then();

        // Stream outgoing messages to client (Kafka -> WS)
        Flux<WebSocketMessage> outbound = outgoingSink.asFlux()
                .map(session::textMessage)
                .doOnCancel(() -> cleanupSession(session));

        return session.send(outbound)
                .and(inbound)
                .doFinally(sig -> log.info("websocket session {} closed: {}", session.getId(), sig));
    }

    /**
     * Handles client-side messages (subscribe / unsubscribe / publish)
     */
    private Mono<Void> handleIncoming(WebSocketSession session, String payload) {
        try {
            IWsIncoming incoming = objectMapper.readValue(payload, IWsIncoming.class);

            return switch (incoming) {
                case WsSubscribe s -> doSubscribe(session, s);
                case WsUnsubscribe u -> doUnsubscribe(session, u);
                case WsPublish p -> doPublish(p);
                default -> Mono.empty();
            };
        } catch (Exception e) {
            log.error("failed to parse websocket message: {}", e.getMessage());
            return Mono.empty();
        }
    }

    /**
     * Subscribe WebSocket session to Kafka topic
     */
    private Mono<Void> doSubscribe(WebSocketSession session, WsSubscribe subscribe) {
        String topic = subscribe.getTopic();

        if (sessionSubscriptions.containsKey(topic)) {
            log.debug("session {} already subscribed to {}", session.getId(), topic);
            return Mono.empty();
        }

        log.info("session {} subscribing to topic {}", session.getId(), topic);

        Disposable subscription = kafkaClusterService.subscribe(topic)
                .map(this::serializeToText)
                .doOnNext(outgoingSink::tryEmitNext)
                .doOnError(e -> log.error("error streaming kafka messages for {}: {}", topic, e.getMessage()))
                .subscribe();

        sessionSubscriptions.put(topic, subscription);
        return Mono.empty();
    }

    /**
     * Unsubscribe from a topic
     */
    private Mono<Void> doUnsubscribe(WebSocketSession session, WsUnsubscribe unsubscribe) {
        String topic = unsubscribe.getTopic();
        Disposable sub = sessionSubscriptions.remove(topic);
        if (sub != null) {
            sub.dispose();
            log.info("session {} unsubscribed from {}", session.getId(), topic);
        }
        return Mono.empty();
    }

    /**
     * Publish message to Kafka topic
     */
    private Mono<Void> doPublish(WsPublish publish) {
        log.debug("publishing to kafka topic {}: {}", publish.getTopic(), publish.getPayload());

        DeviceEvent outgoing = new DeviceEvent(
                "kafka-msg", //WsMsgType.WS_MSG_TYPE_KAFKA_MSG,
                publish.getTopic(),
                publish.getPayload(),
                publish.getQos(),
                publish.isRetained(),
                Instant.now()
        );

        return kafkaClusterService.publish(outgoing)
                .then()
                .onErrorResume(e -> {
                    log.error("kafka publish failed: {}", e.getMessage());
                    return Mono.empty();
                });
    }

    /**
     * Cleans up session subscriptions and resources
     */
    private void cleanupSession(WebSocketSession session) {
        log.info("cleaning up websocket session {}", session.getId());
        sessionSubscriptions.values().forEach(Disposable::dispose);
        sessionSubscriptions.clear();
    }

    /**
     * Helper: serialize to JSON string
     */
    private String serializeToText(DeviceEvent outgoing) {
        try {
            return objectMapper.writeValueAsString(outgoing);
        } catch (Exception e) {
            log.error("failed to serialize WsOutgoing: {}", e.getMessage());
            return "{}";
        }
    }

    @PreDestroy
    public void onShutdown() {
        log.info("shutting down websocket kafka service...");
        kafkaClusterService.cleanup();  // releases all topic sinks/subscriptions
    }
}