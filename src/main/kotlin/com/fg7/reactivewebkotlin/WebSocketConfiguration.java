package com.fg7.reactivewebkotlin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;

@Configuration
public class WebSocketConfiguration {

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler webSocketHandler() {
        return webSocketSession -> {
            System.out.println("session");

            Flux<WebSocketMessage> messageFlux =
                    Flux.<Message>generate(sink -> sink.next(new Message("Hello @ " + Instant.now().toString())))
                            .map(g -> webSocketSession.textMessage(g.getText()))
                            .delayElements(Duration.ofSeconds(1))
                            .doFinally(signalType -> System.out.println("Done! Bye!"));

            return webSocketSession.send(messageFlux);
        };
    }

    @Bean
    HandlerMapping handlerMapping (){
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setUrlMap(Collections.singletonMap("/ws/test", webSocketHandler()));
        simpleUrlHandlerMapping.setOrder(1);
        return simpleUrlHandlerMapping;
    }
}
