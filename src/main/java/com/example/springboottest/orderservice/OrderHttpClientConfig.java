package com.example.springboottest.orderservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class OrderHttpClientConfig {

    @Value("${spring.application.order-service.host}")
    private String orderServiceHost;

    //Rest HTTP Interface
    // https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-http-interface
    @Bean
    OrderHttpClient orderHttpClient() {
        WebClient webClient = WebClient.builder().baseUrl(orderServiceHost).build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(OrderHttpClient.class);
    }
}
