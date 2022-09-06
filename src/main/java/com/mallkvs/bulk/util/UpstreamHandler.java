package com.mallkvs.bulk.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component
public class UpstreamHandler {
    private final WebClient webClient;

    public UpstreamHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    //aggregation/1.0.0/shop/289988/item/test
    public Mono<String> getResponse(JsonNode uriParamMap, Map<String, String> headerMap) {
        String shopId = uriParamMap.get("shopId").textValue();
        String manageNumber = uriParamMap.get("manageNumber").textValue();
        URI uri = null;
        try {
            uri = new URI(String.format("aggregation/1.0.0/shop/%s/item/%s", shopId, manageNumber));
        } catch (URISyntaxException ux) {
            //TODO Error Log Here
        }
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class);
    }
}
