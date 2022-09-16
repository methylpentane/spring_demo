package com.mallkvs.bulk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mallkvs.bulk.exception.InvalidRequestException;
import com.mallkvs.bulk.exception.ServiceException;
import com.mallkvs.bulk.exception.UpstreamErrorResponseException;
import com.mallkvs.bulk.model.Response;
import com.mallkvs.bulk.util.UpstreamClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class Aggregator {
    private final UpstreamClient upstreamClient;
    private final ObjectMapper mapper;

    public Aggregator(UpstreamClient upstreamClient, ObjectMapper mapper) {
        this.upstreamClient = upstreamClient;
        this.mapper = mapper;
    }

    @CircuitBreaker(name = "defaultCircuitBreaker")
    public Mono<ResponseEntity<ObjectNode>> callAggregation(JsonNode requestBody, Map<String, String> requestHeader) {
        JsonNode requestBodies = requestBody.get("request");
        List<Mono<Object>> responses = new ArrayList<>();

        if(requestBodies.size() > 20) return Mono.error(new InvalidRequestException("Number of requests exceeds 20"));
        else if(requestBodies.size() == 0) return Mono.error(new InvalidRequestException("No requests"));

        requestBodies.forEach(body -> responses.add(upstreamClient.getResponse(body, requestHeader)));
        return Flux.merge(responses)
                .collectList()
                .flatMap(response-> {
                        ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
                        int okCount = 0;
                        for(int index = 0; index < response.size(); index++) {
                            Object responseObject = response.get(index);
                            if(responseObject instanceof Response) {
                                okCount ++;
                                result.with("result")
                                        .set(String.valueOf(index), mapper.valueToTree(responseObject));
                            }else if(responseObject instanceof UpstreamErrorResponseException) {
                                result.with("result")
                                        .put(String.valueOf(index), ((UpstreamErrorResponseException) responseObject).getMessage());
                            }
                        }
                        int resultStatusCode;
                        if (okCount > 0) {
                            if (okCount == response.size()) resultStatusCode = HttpStatus.OK.value();
                            else resultStatusCode = HttpStatus.MULTI_STATUS.value();
                            return Mono.just(ResponseEntity.status(resultStatusCode).body(result));
                        }else{
                            resultStatusCode = HttpStatus.SERVICE_UNAVAILABLE.value();
                            return Mono.error(new ServiceException(503, "All request failed to retrieve."));
                        }
                })
//                .retryWhen(
//                        Retry.backoff(1, Duration.of(1, ChronoUnit.SECONDS))
//                                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> new ServiceException(503, retrySignal.failure()))
//                )
                .log();
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
