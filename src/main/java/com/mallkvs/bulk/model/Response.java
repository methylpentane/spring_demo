package com.mallkvs.bulk.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private int httpStatus;
    private ObjectNode response;
}
