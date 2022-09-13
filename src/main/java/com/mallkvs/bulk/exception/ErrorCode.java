package com.mallkvs.bulk.exception;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
    INVALID_JSON_FORMAT("KVS2001"),
    RESOURCE_NOT_FOUND("KVS4001"),
    CACHE_KEY_NOT_FOUND("KVS4002"),
    HTTP_METHOD_NOT_ALLOWED("KVS4003"),
    NOT_ACCEPTABLE("KVS4004"),
    WRONG_AUTHENTICATION_INFORMATION("KVS4005"),
    WRONG_AUTHORIZATION_INFORMATION("KVS4006"),
    INVALID_CLIENT_ID("KVS4007"),
    ITEM_NOT_FOUND("KVS4008"),
    INVALID_TRACE_ID("KVS4009"),
    UPSTREAM_CONNECTION_ERROR("KVS5001"),
    UPSTREAM_TIMEOUT_ERROR("KVS5002"),
    CACHE_SERVER_CONNECTION_ERROR("KVS5005"),
    CACHE_SERVER_TIMEOUT_ERROR("KVS5006"),
    CACHE_SERVER_ERROR("KVS5007"),
    BUSINESS_LOGIC_CATEGORY_MAPPING_ERROR("KVS1001"),
    BUSINESS_LOGIC_PARTIAL_CATEGORY_ERROR("KVS1002"),
    BUSINESS_LOGIC_INVALID_CATEGORY_DATA("KVS1003"),
    ;
    private final String code;
    private static final Map<String, ErrorCode> ERROR_CODE_BY_LABEL = new HashMap<>();

    static {
        for (ErrorCode e : values()) {
            ERROR_CODE_BY_LABEL.put(e.code, e);
        }
    }

    ErrorCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return this.code;
    }

    public static ErrorCode valueOfLabel(String label) {
        return ERROR_CODE_BY_LABEL.get(label);
    }
}
