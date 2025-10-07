//package services;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.whl.quickjs.wrapper.JSCallFunction;
//import com.whl.quickjs.wrapper.JSObject;
//import com.whl.quickjs.wrapper.QuickJSContext;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//
//public class QuickJsPool implements AutoCloseable {
//    private final BlockingQueue<QuickJSContext> pool;
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public QuickJsPool(int size) {
//        pool = new ArrayBlockingQueue<>(size);
//        for (int i = 0; i < size; i++) {
//            pool.add(QuickJSContext.create());
//        }
//    }
//
//    public Mono<List<String>> evalRule(String script, JsonNode telemetry, int timeoutMs) {
//        return Mono.fromCallable(() -> {
//                    QuickJSContext ctx = pool.take();
//                    try {
//                        List<String> emitted = new ArrayList<>();
//
//                        // inject telemetry and emit function
//                        JSObject global = ctx.getGlobalObject();
//                        global.setProperty("telemetry", telemetry);
//                        JSCallFunction emitCb = args -> {
//                            Object arg = args.length > 0 ? args[0] : null;
//                            try {
//                                if (arg == null) {
//                                    emitted.add("null");
//                                } else if (arg instanceof String) {
//                                    emitted.add((String)arg);
//                                } else {
//                                    emitted.add(mapper.writeValueAsString(arg));
//                                }
//                            } catch (Exception e) {
//                                emitted.add("{\"error\":\"emit-conversion\"}");
//                            }
//                            return null;
//                        };
//                        global.setProperty("emit", emitCb);
//
//                        // evaluate (synchronous)
//                        ctx.evaluate(script);
//
//                        // cleanup any JS side objects if needed (wrapper handles GC on destroy)
//                        return emitted;
//                    } finally {
//                        // return context to pool
//                        pool.offer(ctx);
//                    }
//                })
//                .subscribeOn(Schedulers.boundedElastic())
//                .timeout(Duration.ofMillis(timeoutMs));
//    }
//
//    @Override
//    public void close() {
//        pool.forEach(QuickJSContext::destroy);
//    }
//}