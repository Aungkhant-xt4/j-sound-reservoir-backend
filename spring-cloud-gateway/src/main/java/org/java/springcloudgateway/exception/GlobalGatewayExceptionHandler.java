package org.java.springcloudgateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.java.springcloudgateway.model.dto.ApiResponse;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServiceUnavailableException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-1)
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        String path = exchange.getRequest().getPath().value();
        String requestId = exchange.getRequest().getId();

        if (ex instanceof NotFoundException || ex instanceof ServiceUnavailableException || ex.getCause() instanceof IllegalStateException) {
            return writeErrorResponse(exchange, HttpStatus.SERVICE_UNAVAILABLE.value(), "Service Unavailable: " + ex.getMessage(), path, requestId);
        }
        return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", path, requestId);
    }

    public Mono<Void> writeErrorResponse(ServerWebExchange exchange, int status, String message, String path, String requestId) {
        exchange.getResponse().setStatusCode(HttpStatus.valueOf(status));
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<Void> errorResponse = ApiResponse.error(status, message, path, requestId);

        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = mapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            log.error(errorResponse.toString());
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }
}