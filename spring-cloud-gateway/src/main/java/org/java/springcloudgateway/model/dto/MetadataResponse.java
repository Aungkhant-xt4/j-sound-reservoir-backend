package org.java.springcloudgateway.model.dto;

public record MetadataResponse (
        String timestamp,
        String path,
        String requestId
) {
}
