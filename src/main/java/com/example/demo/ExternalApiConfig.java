package com.example.demo;

import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

@Configuration
public class ExternalApiConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://cf159072-904a-439a-9a28-4d25b2340e7f.mock.pstmn.io")
                .build();
    }

    @Bean
    public CircuitBreakerConfigCustomizer externalServiceFooCircuitBreakerConfig() {
        return CircuitBreakerConfigCustomizer
                .of("externalServiceFoo",
                        builder -> builder.slidingWindowSize(10)
                                .slidingWindowType(COUNT_BASED)
                                .waitDurationInOpenState(Duration.ofSeconds(5))
                                .minimumNumberOfCalls(5)
                                .failureRateThreshold(50.0f));
    }
}