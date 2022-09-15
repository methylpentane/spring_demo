package com.mallkvs.bulk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mallkvs.bulk.exception.InvalidRequestException;
import com.mallkvs.bulk.exception.ServiceException;
import com.mallkvs.bulk.model.Response;
import com.mallkvs.bulk.util.UpstreamHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class Aggregator {
    private final UpstreamHandler upstreamHandler;
    private final ObjectMapper mapper;

    public Aggregator(UpstreamHandler upstreamHandler, ObjectMapper mapper) {
        this.upstreamHandler = upstreamHandler;
        this.mapper = mapper;
    }

//        @CircuitBreaker(name = "externalServiceBar")
    public Mono<ResponseEntity<ObjectNode>> callAggregation(JsonNode requestBody, Map<String, String> requestHeader) {
        JsonNode requestBodies = requestBody.get("request");
        List<Mono<Object>> responses = new ArrayList<>();

        if(requestBodies.size() > 20) return Mono.error(new InvalidRequestException("Number of requests exceeds 20"));
        else if(requestBodies.size() == 0) return Mono.error(new InvalidRequestException("No requests"));

        requestBodies.forEach(body -> responses.add(upstreamHandler.getResponse(body, requestHeader)));
        return Flux.merge(responses)
                .collectList()
                .flatMap(response-> {
                        ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
                        int expectedStatus = HttpStatus.OK.value();
                        for(int index = 0; index < response.size(); index++) {
                            Object responseObject = response.get(index);
                            if(responseObject instanceof Response) {
                                result.with("result").set(String.valueOf(index), mapper.valueToTree(responseObject));
                            }else if(responseObject instanceof ServiceException){
                                expectedStatus = HttpStatus.MULTI_STATUS.value();
                                result.with("result").put(String.valueOf(index), ((ServiceException)responseObject).getMessage());
                        }
                        return Mono.just(ResponseEntity.status(expectedStatus).body(result));
                });
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
