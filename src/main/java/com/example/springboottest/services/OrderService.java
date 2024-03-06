package com.example.springboottest.services;

import com.example.springboottest.orderservice.OrderHttpClient;
import com.example.springboottest.orderservice.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
    final OrderHttpClient orderHttpClient;

    public Mono<Order> fetchCustomerOrder(Long id) {
        return orderHttpClient.getOrder(id);
    }

}
