package com.fg7.reactivewebkotlin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ReactiveWebJavaConfig {


    Mono<ServerResponse> sseHandler(ServerRequest req) {
        return ServerResponse.ok()
                .body(Flux.just("Alternative Message: "), String.class);
    }

    Mono<ServerResponse> simpleHandler(ServerRequest req) {
        return ServerResponse.ok()
                .header("x-served-by", "functional-routing-sse")
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(Flux.<Message>generate(sink ->
                        sink.next(new Message("Message @" + Instant.now().toString())))
                        .delayElements(Duration.ofSeconds(1)), Message.class);
    }

    /*
         test with http --stream http://localhost:8080/fr/sse
    */
    @Bean
    RouterFunction<ServerResponse> getRoutes() {
        RouterFunction<ServerResponse> routerFunction = route(
                GET("/fr/message").or(GET("/fr/alternative")), this::sseHandler)
                .andRoute(GET("/fr/sse"), this::simpleHandler);

//        if (someCondition().test("functional")) { // conditionally add another route (use case: round robin routing)
//            // immutable!
//            routerFunction = routerFunction.andRoute(GET("/fr/sse"), this::simpleHandler);
//        }
        return routerFunction;
    }

//    private Predicate<String> someCondition() {
//        return (test) -> test.equals("functional");
//    }
}
