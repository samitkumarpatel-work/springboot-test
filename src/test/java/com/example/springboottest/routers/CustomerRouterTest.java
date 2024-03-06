package com.example.springboottest.routers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
public class CustomerRouterTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withInitScript("db/schema.sql")
            .waitingFor(Wait.forListeningPort());


    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {

    }

    @Test
    void employeeRouterTest() {
        webTestClient
                .get()
                .uri("/customer/{id}", 1)
                .exchange()
                .expectAll(
                        response -> response.expectStatus().isOk(),
                        response -> response.expectBody().json("""
                            {
                            
                            }
                        """)
                );
    }

}
