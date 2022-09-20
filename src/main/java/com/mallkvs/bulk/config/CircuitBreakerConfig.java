package com.mallkvs.bulk.config;

import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

@Configuration
public class CircuitBreakerConfig {
    /**
     * Circuit Breaker bean
     * This can be used with adding @CircuitBreaker on service method.
     * CircuitBreaker will open (make unavailable) when consecutive exception is returned from the service method.
     * Once CircuitBreaker has opened, access to the service cause CallNotPermittedException.
     * CircuitBreaker will close again after some seconds (duration is configurable).
     */

    @Bean
    public CircuitBreakerConfigCustomizer DefaultCircuitBreakerConfig() {
        return CircuitBreakerConfigCustomizer
                .of("defaultCircuitBreaker",
                        builder -> builder.slidingWindowSize(10)
                                .slidingWindowType(COUNT_BASED)
                                .waitDurationInOpenState(Duration.ofSeconds(5))
                                .minimumNumberOfCalls(5)
                                .failureRateThreshold(50.0f));
    }
}
