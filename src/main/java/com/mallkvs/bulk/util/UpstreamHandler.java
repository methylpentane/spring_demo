package com.mallkvs.bulk.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.mallkvs.bulk.exception.InvalidRequestException;
import com.mallkvs.bulk.exception.ServiceException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component
public class UpstreamHandler {
    static final Logger logger = LogManager.getLogger(UpstreamHandler.class.getName());
    private final WebClient webClient;

    public UpstreamHandler(WebClient webClient) {
        this.webClient = webClient;
    }

    //aggregation/1.0.0/shop/289988/item/test
    public Mono<String> getResponse(JsonNode uriParamMap, Map<String, String> headerMap) {
        String shopId, manageNumber, xClientId, authorization;
        try {
            shopId = uriParamMap.get("shopId").textValue();
            manageNumber = uriParamMap.get("manageNumber").textValue();
        } catch (NullPointerException npe) {
            throw new InvalidRequestException();
        }
        xClientId = headerMap.get("X-Client-Id");
        authorization = headerMap.get("Authorization");
        logger.info("shopId:" + shopId + ", manageNumber:" + manageNumber);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/aggregation_404/1.0.0/shop/{shop}/item/{item}").build(shopId, manageNumber))
                .header("X-Client-Id", xClientId)
                .header("Authorization", authorization)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.SERVICE_UNAVAILABLE.value(),
                        response -> Mono.error(new ServiceException("Not Found Error", response.statusCode().value())))
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("{\"message\", \"error\"}"));
//                .log();

        // sorry, it didn't works
//        URI uri = null;
//        try {
//            uri = new URI(String.format("aggregation/1.0.0/shop/%s/item/%s", shopId, manageNumber));
//        } catch (URISyntaxException ux) {
//            //TODO Error Log Here
//        }

//        return webClient.get()
//                .uri(uri)
//                .retrieve()
//                .bodyToMono(String.class);
    }
}
