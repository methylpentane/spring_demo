package com.mallkvs.bulk.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Configuration
public class WebClientConfig {
    /**
     * webclient bean and default setting
     */

    @Value("${baseUrl}")
    private String baseUrl;
    private final CircuitBreakerConfig circuitBreakerConfig;

    public WebClientConfig(CircuitBreakerConfig circuitBreakerConfig) {
        this.circuitBreakerConfig = circuitBreakerConfig;
    }

    @Bean
    public WebClient webClient() {
        HttpClient client = HttpClient
            .create(ConnectionProvider
                          .builder("fixed")
                          .maxConnections(500)
                          .build())
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
            .doOnConnected(connection -> connection
                          .addHandlerLast(new ReadTimeoutHandler(1000, TimeUnit.MILLISECONDS))
                          .addHandlerLast(new WriteTimeoutHandler(1000, TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(client)).baseUrl(baseUrl).build();
    }

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.of(this.circuitBreakerConfig);
    }
}