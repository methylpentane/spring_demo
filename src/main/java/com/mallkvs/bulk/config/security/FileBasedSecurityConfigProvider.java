package com.mallkvs.bulk.config.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

@Configuration
@Log4j2
public class FileBasedSecurityConfigProvider {
    @Value("${securityConfigFilePath}")
    String configPath;

    List<Object> securityUserConfig;

    /**
     * prepare List of Object(intended as JsonNode) Object for bean.
     */
    @PostConstruct
    public void init() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            TypeReference<List<Object>> typeReference = new TypeReference<>() {};
            log.debug(() -> "Start reading security configuration from file.");
            this.securityUserConfig = mapper.readValue(new File(this.configPath), typeReference);
            log.debug(() -> "Security configuration loaded successful.");
        } catch (Exception exception) {
            log.error(
                    () ->
                            "Unable to parse security configuration from file: "
                                    + this.configPath
                                    + ". Error: "
                                    + exception.getMessage(),
                    exception);
            securityUserConfig = List.of();
        }
    }

    /**
     * bind object to Map and return
     * @return bound map
     */
    public Optional<Map<String, Object>> getConfiguration(){
        return Optional.of(Map.of("securityUserConfig", securityUserConfig));
    }

    /**
     * Bean that provide user database from JSON file (securityUserConfig)
     * @return deserialized json object
     */
    @Bean
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getUserConfigList() {
        log.info(() -> "Creating SecurityConfig Bean");
        Map<String, Object> stringObjectMap =
                getConfiguration()
                        .orElseThrow(
                                () ->
                                        new ApplicationContextException(
                                                "Failed to initialize security configuration."));
        return (List<Map<String, Object>>) stringObjectMap.get("securityUserConfig");
    }
}
