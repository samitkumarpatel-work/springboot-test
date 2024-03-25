package com.example.springboottest.kafka;

import com.example.springboottest.config.KafkaPubSubConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
public class KafkaPubSubTest extends KafkaPubSubConfiguration<Integer, String> {

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
            .waitingFor(Wait.forListeningPort())
            ;
    final static String TOPIC = "test-topic";

    @DynamicPropertySource
    static void kafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.producer.key-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add("spring.kafka.producer.value-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");

        registry.add("spring.kafka.consumer.topic", () -> TOPIC);
        registry.add("spring.kafka.consumer.key-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add("spring.kafka.consumer.value-deserializer", () -> "org.apache.kafka.common.serialization.StringDeserializer");
    }

    @Autowired
    KafkaSender<Integer, String> kafkaSender;

    @Autowired
    KafkaReceiver<Integer, String> kafkaReceiver;

    @Test
    void testPubSub() {
        assertAll(
                () -> assertNotNull(kafkaSender),
                () -> assertNotNull(kafkaReceiver),
                () -> {
                    var messages = Flux.<SenderRecord<Integer, String, Integer>>just(
                            SenderRecord.create(TOPIC, 0, System.currentTimeMillis(), 1, "One", 1),
                            SenderRecord.create(TOPIC, 0, System.currentTimeMillis(), 2, "Two", 2),
                            SenderRecord.create(TOPIC, 0, System.currentTimeMillis(), 3, "Three", 3)
                            );

                    kafkaSender.send(Mono.just(SenderRecord.create(TOPIC, 0, System.currentTimeMillis(), 1, "One", 1)))
                            .doOnError(System.err::println)
                            .doOnNext(r -> System.out.printf("Message : " + r.correlationMetadata() + " send response: "  + r.recordMetadata()))
                            .subscribe();

                }

        );
    }

}
