package com.mallkvs.bulk.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mallkvs.bulk.service.ExternalApi;
import com.mallkvs.bulk.service.Aggregator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {

    private final ExternalApi externalApi;
    private final Aggregator externalApi2;

    public ApiController(ExternalApi externalApi, Aggregator externalApi2) {
        this.externalApi = externalApi;
        this.externalApi2 = externalApi2;
    }

    @GetMapping("/foo")
    public Mono<String> foo() {
        return externalApi.callExternalApiFoo("100000", "234422344");
    }

    @RequestMapping(value = { "/bar", "/" }, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> bar(@RequestBody JsonNode rootNode) {
        return externalApi2.callExternalApiBar(rootNode);
    }
}