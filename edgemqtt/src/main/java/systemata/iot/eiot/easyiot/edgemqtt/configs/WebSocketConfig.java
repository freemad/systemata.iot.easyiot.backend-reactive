package systemata.iot.eiot.easyiot.edgemqtt.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.server.WebFilter;
import systemata.iot.eiot.easyiot.edgemqtt.services.KafkaWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {

    private final KafkaWebSocketHandler kafkaWebSocketHandler;

    /**
     * Maps WebSocket routes to handlers.
     */
    @Bean
    public HandlerMapping webSocketMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/api/v1/ws/mqtt", kafkaWebSocketHandler); // WebSocket endpoint

        // Order must be low to ensure it's picked before WebFlux HTTP routes
        int order = -1;
        return new SimpleUrlHandlerMapping(map, order);
    }

    /**
     * Adapts the reactive WebSocket service for use with Spring WebFlux.
     */
    @Bean
    public WebSocketHandlerAdapter handlerAdapter(WebSocketService webSocketService) {
        return new WebSocketHandlerAdapter(webSocketService);
    }

    /**
     * The WebSocketService handles the actual handshake.
     */
    @Bean
    public WebSocketService webSocketService() {
        // You can wrap it to add custom headers, tracing, etc.
        return new HandshakeWebSocketService();
    }

    /**
     * (Optional) Add a filter to log or handle cross-origin handshake.
     * Enables CORS for WebSocket handshakes in multi-domain setups.
     */
    @Bean
    public WebFilter websocketCorsFilter() {
        return (exchange, chain) -> {
            if (exchange.getRequest().getPath().value().startsWith("/ws/")) {
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Headers", "*");
                exchange.getResponse().getHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            }
            return chain.filter(exchange);
        };
    }
}