package com.mallkvs.bulk.service;


import com.mallkvs.bulk.exception.UserDefinedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class SampleApi {
    private final WebClient webClient;

    @CircuitBreaker(name = "defaultCircuitBreaker")
    public Mono<String> callSampleApi(String shopId, String manageNumber) {
        try {
            return webClient
                    .get()
                    .uri("/hello").retrieve()
                    .bodyToMono(String.class)
                    .onErrorResume(Mono::error)
                    .retryWhen(
                            Retry.backoff(3, Duration.of(1, ChronoUnit.SECONDS))
                                    .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new UserDefinedException(retrySignal.failure()))
                    );
        } catch(UserDefinedException ude) {
            return Mono.just(ude.getMessage());
        }
    }
}