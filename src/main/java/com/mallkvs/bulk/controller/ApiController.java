package com.mallkvs.bulk.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mallkvs.bulk.service.SampleApi;
import com.mallkvs.bulk.service.Aggregator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class ApiController {

    private final SampleApi sampleApi;
    private final Aggregator aggregator;

    public ApiController(SampleApi sampleApi, Aggregator aggregator) {
        this.sampleApi = sampleApi;
        this.aggregator = aggregator;
    }

    @GetMapping("/sample")
    // This is an endpoint for test, not for production.
    public Mono<String> foo() {
        return sampleApi.callSampleApi("100000", "234422344");
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.POST, produces = "application/json")
    public Mono<ResponseEntity<ObjectNode>> bar(@RequestBody JsonNode rootNode, @RequestHeader Map<String, String> requestHeader) {
        return aggregator.callAggregation(rootNode, requestHeader);
    }
}