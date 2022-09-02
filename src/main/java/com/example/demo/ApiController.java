package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {

    private final ExternalApi externalApi;
    private final ExternalApi2 externalApi2;

    public ApiController(ExternalApi externalApi, ExternalApi2 externalApi2) {
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