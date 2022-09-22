package com.mallkvs.bulk.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mallkvs.bulk.exception.InvalidRequestException;
import com.mallkvs.bulk.exception.ServiceException;
import com.mallkvs.bulk.exception.UpstreamErrorResponseException;
import com.mallkvs.bulk.exception.UpstreamTimeoutException;
import com.mallkvs.bulk.model.Response;
import com.mallkvs.bulk.util.UpstreamClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    /**
     * aggregator service of this server.
     * validate request, send and collect each request from upstream.
     * return aggregated result to client.
     */
    private final UpstreamClient upstreamClient;
    private final ObjectMapper mapper;
    static final Logger logger = LogManager.getLogger(Aggregator.class.getName());

    public Aggregator(UpstreamClient upstreamClient, ObjectMapper mapper) {
        this.upstreamClient = upstreamClient;
        this.mapper = mapper;
    }

//    @CircuitBreaker(name = "defaultCircuitBreaker")
    public Mono<ResponseEntity<ObjectNode>> callAggregation(JsonNode requestBody, Map<String, String> requestHeader) {

        // extract requests for upstream
        JsonNode requestBodies = requestBody.get("request");
        List<Mono<Object>> responses = new ArrayList<>();

        // request param check
        if(requestBodies.size() > 20) return Mono.error(new InvalidRequestException("Number of requests exceeds 20"));
        else if(requestBodies.size() == 0) return Mono.error(new InvalidRequestException("No requests"));

        // retrieve not in parallel (temporal implementation)
        requestBodies.forEach(body -> responses.add(upstreamClient.getResponse(body, requestHeader)));
        // aggregate
        return Flux.merge(responses)
                .collectList()
                .flatMap(response-> {
                        ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
                        int okCount = 0;
                        // check each response, append to result
                        for(int index = 0; index < response.size(); index++) {
                            Object responseObject = response.get(index);
                            logger.info(responseObject.toString());
                            if(responseObject instanceof Response) {
                                okCount ++;
                                result.with("result")
                                        .set(String.valueOf(index), mapper.valueToTree(responseObject));
                            }else if(responseObject instanceof UpstreamErrorResponseException) {
                                result.with("result")
                                        .put(String.valueOf(index), ((UpstreamErrorResponseException) responseObject).getMessage());
                            }else if(responseObject instanceof UpstreamTimeoutException) {
                                result.with("result")
                                        .put(String.valueOf(index), ((UpstreamTimeoutException) responseObject).getMessage());
                            }
                        }
                        // decide final status, return
                        int resultStatusCode;
                        if (okCount > 0) {
                            if (okCount == response.size()) resultStatusCode = HttpStatus.OK.value();
                            else resultStatusCode = HttpStatus.MULTI_STATUS.value();
                            return Mono.just(ResponseEntity.status(resultStatusCode).body(result));
                        }else{
                            resultStatusCode = HttpStatus.SERVICE_UNAVAILABLE.value();
                            return Mono.error(new ServiceException(503, "All request failed to retrieve."));
                        }
                });
    }
}
