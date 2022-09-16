package com.mallkvs.bulk.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mallkvs.bulk.service.Aggregator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class ApiController {

    private final Aggregator aggregator;

    public ApiController(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    @RequestMapping(value = {"/"}, method = RequestMethod.POST, produces = "application/json")
    public Mono<ResponseEntity<ObjectNode>> bar(@RequestBody JsonNode rootNode, @RequestHeader Map<String, String> requestHeader) {
        return aggregator.callAggregation(rootNode, requestHeader);
    }
}