package com.example.demo;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class ExternalApi2 {
    private final WebClient webClient;

//    @CircuitBreaker(name = "externalServiceBar")
    public Mono<String> callExternalApiBar(List<Map> requests) {
        List<Mono> responses = new ArrayList<Mono>();
        for (Map req: requests) {
            String shopId = (String) req.get("shopId");
            String manageNumber = (String) req.get("managenumber");
//            System.out.println(shopId + manageNumber);
            responses.add(webClient.get().uri("/shop").retrieve().bodyToMono(String.class));
        }
        return Flux.concat(responses);
//        return webClient.get().uri("/shop").retrieve()
//                .bodyToMono(String.class);
    }
}
