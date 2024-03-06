package com.example.springboottest.handlers;

import com.example.springboottest.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    final CustomerService customerService;

    public Mono<ServerResponse> customerById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));

        return customerService.fetchCustomer(id)
                .flatMap(ServerResponse.ok()::bodyValue)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
