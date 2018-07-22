package com.fg7.reactivewebkotlin;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;

@RestController
public class MessageRestController {

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Publisher<Message> sseMessage () {
        return Flux.<Message>generate(sink -> sink.next(new Message("Hello world @" + Instant.now().toString())))
        .delayElements(Duration.ofSeconds(1));
    }

    @GetMapping("/greetings")
    Publisher<Message> messagePublisher() {
        return Flux.<Message>generate(sink -> sink.next(new Message("Hello, world"))).take(1000);
    }
}


