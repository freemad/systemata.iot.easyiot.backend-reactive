package systemata.iot.eiot.easyiot.edgemqtt.services;

// File: src/main/java/com/example/iot/websocket/MqttWebSocketHandler.java

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import systemata.iot.eiot.easyiot.common.domain.models.MqttMessageEnvelope;
import systemata.iot.eiot.easyiot.edgemqtt.contracts.domain.dtos.IWsIncoming;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsOutgoing;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsPublish;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsSubscribe;
import systemata.iot.eiot.easyiot.edgemqtt.domain.dtos.WsUnsubscribe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqttWebSocketHandler
        implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final MqttService mqttService;

    // incoming messages from the client are JSON; we'll deserialize to WsIncoming
    @Override
    public Mono<Void> handle(WebSocketSession session) {

        // track per-session subscriptions so we can dispose on close/unsubscribe
        Map<String, Disposable> sessionSubscriptions = new ConcurrentHashMap<>();
        // composite disposable to cleanup all on close
        Disposable.Composite composite = Disposables.composite();

        // outboundFlux: messages from all MQTT subscriptions for this session
        Flux<WebSocketMessage> outboundFlux = session.receive()
                .publishOn(Schedulers.boundedElastic())
                // handle incoming client messages
                .flatMap(webSocketMessage -> {
                    String payload = webSocketMessage.getPayloadAsText();
                    try {
                        IWsIncoming incoming = objectMapper.readValue(payload, IWsIncoming.class);

                        switch (incoming) {
                            case WsSubscribe subscribe -> wsSubscribe(session, subscribe, sessionSubscriptions, composite);
                            case WsUnsubscribe unsubscribe -> wsUnsubscribe(session, unsubscribe, sessionSubscriptions);
                            case WsPublish publish ->  wsPublish(session, publish);
                            case null, default -> {
                                log.warn("unknown incoming type: {}", incoming);
                                return Mono.empty();
                            }
                        }
                    } catch (Exception ex) {
                        log.error("failed to process incoming ws message: {}", ex.getMessage());
                        return Mono.empty();
                    }
                    return Mono.empty();
                })
                // We used direct subscription above; we still need a heartbeat to keep session.send working.
                .thenMany(Flux.never()); // ensure outboundFlux remains (we'll actually produce messages differently)

        /*
         * The approach above started per-topic Flux subscriptions but didn't connect them into `session.send`.
         *
         * Better approach: rather than subscribing inside the receive flatMap, we:
         * - create a merged outgoing Flux from per-topic sinks that are added on subscribe,
         * - then session.send(outgoingFlux) will push messages to client.
         *
         * Let's instead create a dynamic emitter subject for this session and route MQTT events into it.
         */

        // dynamic outgoing sink for this session
        var outgoingSink = Sinks.many().multicast().onBackpressureBuffer();

        // Rework: when a subscribe arrives, add a mapping to forward to outgoingSink (not subscribe directly)
        // So we must reimplement receive handling to add/remove forwarders.

        // Clean approach: use `session.receive()` to process control messages, and maintain map of forwarders (Disposable)
        Mono<Void> control = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .flatMap(payload -> {
                    try {
                        IWsIncoming incoming = objectMapper.readValue(payload, IWsIncoming.class);
                        if (incoming instanceof WsSubscribe s) {
                            String topic = s.getTopic();
                            if (sessionSubscriptions.containsKey(topic)) {
                                return Mono.empty();
                            }
                            // create forward: mqttService.subscribe(topic) -> map to WS json -> emit into outgoingSink
                            Disposable forward = mqttService.subscribe(topic)
                                    .map(this::toWsOutgoing)
                                    .map(this::toJsonText)
                                    .subscribe(outgoingSink::tryEmitNext);
                            sessionSubscriptions.put(topic, forward);
                            composite.add(forward);
                            return Mono.empty();
                        } else if (incoming instanceof WsUnsubscribe u) {
                            String topic = u.getTopic();
                            Disposable d = sessionSubscriptions.remove(topic);
                            if (d != null) d.dispose();
                            return Mono.empty();
                        } else if (incoming instanceof WsPublish p) {
                            return mqttService.publish(p.getTopic(), p.getPayload(), p.getQos(), p.isRetained()).then();
                        } else {
                            return Mono.empty();
                        }
                    } catch (Exception e) {
                        log.error("Failed to parse WS incoming: {}", e.getMessage());
                        return Mono.empty();
                    }
                }).then();

        // outbound Flux from outgoingSink as WebSocketMessage
        Flux<WebSocketMessage> outbound = outgoingSink.asFlux()
                .map(json -> session.textMessage(json))
                .doOnCancel(() -> {
                    // cleanup when client disconnects or cancels
                    composite.dispose();
                    sessionSubscriptions.values().forEach(Disposable::dispose);
                    sessionSubscriptions.clear();
                });

        // combine control handling and outbound send
        return Mono.zip(control, session.send(outbound)).then()
                .doFinally(sig -> {
                    // ensure cleanup
                    composite.dispose();
                    sessionSubscriptions.values().forEach(Disposable::dispose);
                    sessionSubscriptions.clear();
                });
    }

    private Mono<Object> wsPublish(WebSocketSession session, WsPublish publish) {
        log.info("WS {} publish to {} payload {}", session.getId(), publish.getTopic(), publish.getPayload());
        return mqttService.publish(publish.getTopic(), publish.getPayload(), publish.getQos(), publish.isRetained())
                .then(Mono.empty());
    }

    private static Mono<Object> wsUnsubscribe(WebSocketSession session, WsUnsubscribe unsubscribe, Map<String, Disposable> sessionSubscriptions) {
        String topic = unsubscribe.getTopic();
        log.info("WS {} unsubscribe {} ", session.getId(), topic);
        Disposable d = sessionSubscriptions.remove(topic);
        if (d != null) d.dispose();
        return Mono.empty();
    }

    private Mono<Object> wsSubscribe(WebSocketSession session, WsSubscribe subscribe, Map<String, Disposable> sessionSubscriptions, Disposable.Composite composite) throws MqttException {
        String topic = subscribe.getTopic();
        log.info("ws {} subscribe {} ", session.getId(), topic);

        // If already subscribed, ignore
        if (!sessionSubscriptions.containsKey(topic)) {
            Flux<WebSocketMessage> msgs = mqttService.subscribe(topic)
                    .map(this::toWsOutgoing)
                    .map(this::toJsonText)
                    .map(session::textMessage)
                    .onErrorResume(e -> {
                        log.error("subscribe error for {}: {}", topic, e.getMessage());
                        return Mono.empty();
                    });

            // Subscribe to the stream and keep track (we subscribe but we also return nothing here)
            Disposable d = msgs.publishOn(Schedulers.boundedElastic()).subscribe(); // start pushing directly to client via session.send below
            sessionSubscriptions.put(topic, d);
            composite.add(d);
        }
        return Mono.empty();
    }

    private WsOutgoing toWsOutgoing(MqttMessageEnvelope env) {
        return new WsOutgoing("mqtt-message", env.getTopic(), env.getPayload(), env.getQos(), env.getRetained(), env.getReceivedAt());
    }

    @SneakyThrows
    private String toJsonText(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }
}
