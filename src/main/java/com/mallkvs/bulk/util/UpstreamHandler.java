package com.mallkvs.bulk.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mallkvs.bulk.exception.InvalidRequestException;
import com.mallkvs.bulk.exception.ServiceException;
import com.mallkvs.bulk.model.Response;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Component
public class UpstreamHandler {
    @Value("${aggregationEndpoint}")
    private String aggregationEndpoint;
    private final WebClient webClient;
    static final Logger logger = LogManager.getLogger(UpstreamHandler.class.getName());

    public UpstreamHandler(WebClient webClient) {
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
                        clientResponse -> {
                            if (clientResponse.statusCode().isError()) {
                                // when error occur expected to have error string
                                return clientResponse
                                        .bodyToMono(String.class)
                                        .map(
                                                string -> new ServiceException(
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
                        });
                /* TODO Exception handling template
                .onErrorResume(
                        throwable -> {
                            if (throwable instanceof ServiceException) {
                                return Mono.error(throwable);
                            } else {
                                return Mono.error(new UpstreamTimeoutException("Aggregation-Service", throwable));
                            }
                        });
                .log();
                 */
    }
}
