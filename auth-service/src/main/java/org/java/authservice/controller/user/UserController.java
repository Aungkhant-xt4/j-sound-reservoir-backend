package org.java.authservice.controller.user;

import lombok.RequiredArgsConstructor;
import org.java.authservice.service.user.UserService;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/id")
    public ResponseEntity<ApiResponse<Long>> getUserIdByEmail(@RequestParam String email) {
        Long userId = userService.getUserIdByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(userId));
    }
}
