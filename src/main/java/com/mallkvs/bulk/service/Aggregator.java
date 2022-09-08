package com.mallkvs.bulk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.mallkvs.bulk.util.UpstreamHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class Aggregator {
    private final UpstreamHandler upstreamHandler;

    public Aggregator(UpstreamHandler upstreamHandler) {
        this.upstreamHandler = upstreamHandler;
    }

    //    @CircuitBreaker(name = "externalServiceBar")
    public Mono<String> callExternalApiBar(JsonNode requestBody, Map<String, String> requestHeader) {
        JsonNode requestBodies = requestBody.get("request");
        List<Mono<String>> responses = new ArrayList<>();

        requestBodies.forEach(body -> responses.add(upstreamHandler.getResponse(body, requestHeader)));
        return Flux.merge(responses)
                .collectList()
                .flatMap(response-> Mono.just("[" + String.join(",", response) + "]"));
        /* This is completely parallel, but difficult to refactor now.
        return Flux.fromIterable(requests)
                .parallel()
                .flatMap((req) -> upstreamHandler.getResponse(req), Map.of("x", "y"))
                .sequential()
                .collectList()
                .map(List::toString);
         */
    }
}
