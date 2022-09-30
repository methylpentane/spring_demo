package com.mallkvs.bulk.config.security;

import com.mallkvs.bulk.exception.InvalidRequestException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@Log4j2
public class WebFluxSecurityConfig {
    /**
     * basic authentication config
     */

    private final List<Map<String, Object>> userList;

    public WebFluxSecurityConfig(List<Map<String, Object>> userList) {
        this.userList = userList;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * This Bean provides UserDetailsService that is reactive
     * @param passwordEncoder passwordEncoder to use
     * @return reactive UserDetailsService
     */
    @Bean
    public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        Map<String, UserDetails> users = new HashMap<>();
        userList.forEach(userInfo -> {
                users.put(userInfo.get("username").toString(),
                          User.withUsername(userInfo.get("username").toString())
                                  .password(this.passwordEncoder()
                                          .encode(
                                                  new String(Base64.getDecoder().decode(userInfo.get("password").toString()))
                                          )
                                  )
                                  .roles(userInfo.get("role").toString())
                                  .build()
                );
        });
        return new MapReactiveUserDetailsService(users);
    }

    /**
     * Bean that provides security filter chain
     * @return Http filter chain
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .anyExchange().authenticated()
                )
                .addFilterBefore(new ClientIdValidator(), SecurityWebFiltersOrder.LAST)
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

}

