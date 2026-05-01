package org.java.commonlibrary.model.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Request successful", data);
    }
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message, null);
    }
    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }
}