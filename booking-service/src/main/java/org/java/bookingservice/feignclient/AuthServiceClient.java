package org.java.bookingservice.feignclient;

import org.java.commonlibrary.model.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", path = "/api/users")
public interface AuthServiceClient {

    @GetMapping("/id")
    ApiResponse<Long> getUserIdByEmail(@RequestParam("email") String email);
}