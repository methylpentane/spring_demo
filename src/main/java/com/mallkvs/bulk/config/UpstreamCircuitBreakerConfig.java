package com.mallkvs.bulk.config;

import com.mallkvs.bulk.exception.UpstreamTimeoutException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

@Configuration
public class UpstreamCircuitBreakerConfig {
    /**
     * Circuit Breaker Config bean
     */
    @Bean
    public CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .maxWaitDurationInHalfOpenState(Duration.ofSeconds(10))
                .slowCallRateThreshold(50)
                .slowCallDurationThreshold(Duration.ofMillis(1500))
                .failureRateThreshold(50)
                .minimumNumberOfCalls(5)
                .permittedNumberOfCallsInHalfOpenState(5)
                .slidingWindowSize(5)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .automaticTransitionFromOpenToHalfOpenEnabled(false)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .recordExceptions()
                .ignoreExceptions()
                .build();
    }

}
