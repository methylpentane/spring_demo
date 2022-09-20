package com.mallkvs.bulk.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mallkvs.bulk.exception.InvalidRequestException;
import com.mallkvs.bulk.exception.UpstreamErrorResponseException;
import com.mallkvs.bulk.exception.UpstreamTimeoutException;
import com.mallkvs.bulk.model.Response;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component
public class UpstreamClient {
    /**
     * A component that has role of retrieving single response from upstream.
     * it validates parameter and retrieve from upstream.
     * When error occurs, it won't return Mono.error() because error result will also be aggregated by service.
     */
    @Value("${aggregationEndpoint}")
    private String aggregationEndpoint;
    @Value("${timeoutMillis}")
    private int timeoutMillis;
    private final WebClient webClient;
    static final Logger logger = LogManager.getLogger(UpstreamClient.class.getName());

    public UpstreamClient(WebClient webClient) {
        this.webClient = webClient;
    }

    //aggregation/1.0.0/shop/289988/item/test
    public Mono<Object> getResponse(JsonNode uriParamMap, Map<String, String> headerMap) {
        String shopId, manageNumber, xClientId, authorization;
        // parameter
        try {
            shopId = uriParamMap.get("shopId").textValue();
            manageNumber = uriParamMap.get("manageNumber").textValue();
        } catch (NullPointerException npe) {
            return Mono.error(new InvalidRequestException("There is missing parameter."));
        }
        logger.info("shopId:" + shopId + ", manageNumber:" + manageNumber);
        // header
        xClientId = headerMap.get("X-Client-Id");
        authorization = headerMap.get("Authorization");

        // upstream
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(aggregationEndpoint).build(shopId, manageNumber))
                .header("X-Client-Id", xClientId)
                .header("Authorization", authorization)
                .exchangeToMono(
                        // if statement, on status from upstream
                        clientResponse -> {
                            if (clientResponse.statusCode().isError()) {
                                // when error occur expected to have error string
                                return clientResponse
                                        .bodyToMono(String.class)
                                        .map(
                                                string -> new UpstreamErrorResponseException(
                                                        clientResponse.rawStatusCode(),
                                                        string)
                                        );
                            } else {
                                // usually expected to have json object
                                return clientResponse
                                        .bodyToMono(ObjectNode.class)
                                        .map(
                                                objectNode -> new Response(
                                                        clientResponse.rawStatusCode(),
                                                        objectNode)
                                        );
                            }
                        })
                .timeout(Duration.ofMillis(timeoutMillis), Mono.just("Timeout has occurred.").map(UpstreamTimeoutException::new));
    }
}
