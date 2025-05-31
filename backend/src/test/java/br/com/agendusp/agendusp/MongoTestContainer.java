package br.com.agendusp.agendusp;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;


@Testcontainers
@SpringBootTest(classes = MongoTest.class)
public abstract class MongoTestContainer {
    @Container
    static MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer("mongo:latest").withExposedPorts(27017);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (mongoDBContainer != null) {
                mongoDBContainer.stop();
            }
        }));
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("srping.data.mongodb.uri", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
    }
    

}
