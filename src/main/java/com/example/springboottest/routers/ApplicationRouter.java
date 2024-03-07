package com.example.springboottest.routers;

import com.example.springboottest.handlers.CustomerHandler;
import com.example.springboottest.handlers.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ApplicationRouter {

    @Bean
    RouterFunction<ServerResponse> routerFunction(MessageHandler messageHandlers, CustomerHandler customerHandler) {
        return RouterFunctions
                .route()
                .path("/message", messageBuilder -> messageBuilder
                        .GET("", messageHandlers::greet)
                )
                .path("/customer", customerBuilder -> customerBuilder
                        .GET("", request -> ServerResponse.noContent().build())
                        .POST("", customerHandler::saveCustomer)
                        .GET("/{id}", customerHandler::customerById)
                        .PUT("/{id}", request -> ServerResponse.noContent().build())
                        .DELETE("/{id}", request -> ServerResponse.noContent().build())
                )
                .build();
    }
}
