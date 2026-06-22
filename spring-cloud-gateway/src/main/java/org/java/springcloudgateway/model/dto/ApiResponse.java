package org.java.springcloudgateway.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        int status,
        String message,
        T data,
        MetadataResponse metadata
) {
    public static <T> ApiResponse<T> error(int status, String message, String path, String requestId) {
        MetadataResponse meta = new MetadataResponse(java.time.Instant.now().toString(), path, requestId);
        return new ApiResponse<>(status, message, null, meta);
    }
}