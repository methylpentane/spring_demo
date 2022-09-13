package com.mallkvs.bulk.exception;

public class ErrorMessage {
    public static final String RESOURCE_NOT_FOUND =
            "Resource Not found, Wrong Endpoint or PathParameters";
    public static final String CACHE_KEY_NOT_FOUND = "Cache Key is not found.";
    public static final String HTTP_METHOD_NOT_ALLOWED = "%s HTTP Method not allowed.";
    public static final String NOT_ACCEPTABLE = "%s is not the acceptable value.";
    public static final String WRONG_AUTHENTICATION_INFORMATION =
            "Wrong Authentication information (Header)";
    public static final String WRONG_AUTHORIZATION_INFORMATION = "Wrong Authorization information";
    public static final String INVALID_CLIENT_ID =
            "\"X-Client-Id\" is Empty or Not Exist or In-Valid Format.";
    public static final String INVALID_TRACE_ID = "\"X-Trace-Id\" is not with proper format.";
    public static final String ITEM_NOT_FOUND = "Item Not Found";
    public static final String UPSTREAM_CONNECTION_ERROR = "Mall KVS Couldn't connect to %s API.";
    public static final String UPSTREAM_TIMEOUT_ERROR = "Mall KVS connection timeout with %s API.";
    public static final String CACHE_SERVER_CONNECTION_ERROR = "Error Connecting to Cache Server";
    public static final String BUSINESS_LOGIC_CATEGORY_MAPPING_ERROR =
            "Failure to get response from itemCategoryMapping API.";
    public static final String BUSINESS_LOGIC_ALL_CATEGORY_ERROR =
            "Failure to get all the responses from category API.";
    public static final String BUSINESS_LOGIC_PARTIAL_CATEGORY_ERROR =
            "Failure to get few correct responses from category API.";
    public static final String BUSINESS_LOGIC_INVLIAD_DATA_ERROR =
            "The data from %s API Data might be out of date or wrong.";
    public static final String INVALID_JSON_FORMAT_ERROR = "%s API response format is wrong";

    private ErrorMessage() {}
}
