package com.example.springboottest.handlers;

import com.example.springboottest.services.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class MetadataHandler {
    final MetadataService metadataService;
    public Mono<ServerResponse> apiMetadata(ServerRequest request) {
        return metadataService
                .getMetadata()
                .flatMap(metadata -> ServerResponse.ok().bodyValue(metadata));
    }
}
