package com.mallkvs.bulk.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mallkvs.bulk.exception.InvalidRequestException;
import com.mallkvs.bulk.model.Response;
import com.mallkvs.bulk.service.ExternalApi;
import com.mallkvs.bulk.service.Aggregator;
import jdk.jfr.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class ApiController {

    private final ExternalApi externalApi;
    private final Aggregator aggregator;

    public ApiController(ExternalApi externalApi, Aggregator aggregator) {
        this.externalApi = externalApi;
        this.aggregator = aggregator;
    }

    @GetMapping("/foo")
    // This is an endpoint for test, not for production.
    public Mono<String> foo() {
        return externalApi.callExternalApiFoo("100000", "234422344");
    }

    @RequestMapping(value = { "/bar", "/" }, method = RequestMethod.POST, produces = "application/json")
    public Mono<ResponseEntity<ObjectNode>> bar(@RequestBody JsonNode rootNode, @RequestHeader Map<String, String> requestHeader) {
//        String xClientId = requestHeader.getOrDefault("X-Client-Id", "");
//        if (!xClientId.equals("mallkvs")) { throw new InvalidRequestException(); }
        return aggregator.callExternalApiBar(rootNode, requestHeader);
    }
}