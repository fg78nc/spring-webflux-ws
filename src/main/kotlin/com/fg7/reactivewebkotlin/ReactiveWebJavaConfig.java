package com.fg7.reactivewebkotlin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.util.function.Predicate;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class ReactiveWebJavaConfig {

    @Bean
    RouterFunction<ServerResponse> getRoutes() {
        RouterFunction<ServerResponse> routerFunction = route(
                GET("/message").or(GET("/alternative")),
                req -> ok().body(Flux.just("Message: "), String.class));

        if (someCondition().test("test")) { // conditionally add another route (use case: round robin routing)
            routerFunction.andRoute(GET("/extra"), req -> ok().body(Flux.just("Extra: "), String.class));
        }
        return routerFunction;
    }

    private Predicate<String> someCondition() {
        return (test) -> test.equals("test");
    }
}
