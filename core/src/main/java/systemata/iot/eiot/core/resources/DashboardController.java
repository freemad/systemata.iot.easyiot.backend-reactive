//package systemata.iot.eiot.easyiot.common.resources;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/api/dashboard")
//public class DashboardController {
//
//    @GetMapping("/status")
//    public Mono<String> getStatus() {
//        return Mono.just("Service OK");
//    }
//
//    @MessageMapping("/device-data")
//    @SendTo("/topic/updates")
//    public String broadcastDeviceData(String message) {
//        return "Received: " + message;
//    }
//}
