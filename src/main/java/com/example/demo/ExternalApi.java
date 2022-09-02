package com.example.demo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.StringBufferInputStream;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ExternalApi {
    private final WebClient webClient;

    @CircuitBreaker(name = "externalServiceFoo")
    public Mono<String> callExternalApiFoo(String shopId, String manageNumber) {
        try {
            return webClient
                    .get()
                    .uri("/external-foo").retrieve()
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