package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApiController {
    private final ExternalApi externalApi;
    private final ExternalApi2 externalApi2;
    @GetMapping("/foo")
    public Mono<String> foo() {
        return externalApi.callExternalApiFoo();
    }

    @GetMapping("/bar")
    public Mono<String> bar(@RequestBody Map requests) {
        return externalApi2.callExternalApiBar((List<Map>)requests.get("request"));
    }
}