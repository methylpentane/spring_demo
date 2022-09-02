package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class ExternalApi2 {
    private final WebClient webClient;



    //    @CircuitBreaker(name = "externalServiceBar")
    public Mono<String> callExternalApiBar(JsonNode requestBody) {
        JsonNode requests = requestBody.get("request");
        return Flux.fromIterable(requests)
                .parallel()
                .flatMap((req) -> webClient.get().uri("/external-foo").retrieve().bodyToMono(String.class))
                .sequential()
                .collectList()
                .map(List::toString);
//        JsonNode requests = requestBody.get("request");
//        List<Mono<String>> responses = new ArrayList<>();
//        for (JsonNode req: requests) {
//            String shopId = req.get("shopId").textValue();
//            String manageNumber = req.get("manageNumber").textValue();
////            Mono<String> responseMono = externalApi.callExternalApiFoo(shopId, manageNumber);
//            Mono<String> responseMono = webClient.get().uri("/shop").retrieve().bodyToMono(String.class);
//            responses.add(responseMono);
//        }
//        return Flux.merge(responses)
//                .collectList()
//                .flatMap(response-> {
//                    String combinedResponse = "";
//                    for (String res : response) {
//                        combinedResponse += res;
//                    }
//                return Mono.just(combinedResponse);
//        });
    }
}
