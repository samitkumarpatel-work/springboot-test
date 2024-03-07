package com.example.springboottest.routers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
public class CustomerRouterTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withInitScript("db/schema-n-data.sql")
            .waitingFor(Wait.forListeningPort());

    @DynamicPropertySource
    static void applicationProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.application.order-service.host", () -> "http://localhost:${wiremock.server.port}");
    }

    @BeforeEach
    void setUp() {
        //wiremock
        stubFor(get(urlPathEqualTo("/order/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("""
                                    {
                                        "id": 1,
                                        "placedAt": "2021-08-01",
                                        "expectedDeliveryDate": "2021-08-04",
                                        "deliveredDate": null,
                                        "items": ["item1", "item2"]
                                    }
                                """)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)));
    }

    @Test
    void employeeRouterTest(@Autowired WebTestClient webTestClient) {
        webTestClient
                .get()
                .uri("/customer/{id}", 1)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.order.id").isEqualTo(1)
                .json("""
                        {
                            "id":1,
                            "firstName":"John",
                            "lastName":"Doe",
                            "age":30,
                            "dateOfBirth":"1990-01-01",
                            "order":{
                                "id":1,
                                "placedAt":"2021-08-01",
                                "expectedDeliveryDate":"2021-08-04",
                                "deliveredDate":null,
                                "items":["item1","item2"]
                            },
                            "isActive":true
                        }
                """);

    }

}
