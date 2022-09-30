package com.mallkvs.bulk.config.security;

import com.mallkvs.bulk.exception.InvalidRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Log4j2
public class ClientIdValidator implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String client = headers.getFirst("X-Client-Id");
        if (Objects.isNull(client)) {
            String msg = "X-Client-Id Header not found in the request";
            log.info(() -> msg);
            return Mono.error(new InvalidRequestException(msg));
            // TODO it won't be caught by ControllerAdvice.
        }
        return chain.filter(exchange);
    }
}
