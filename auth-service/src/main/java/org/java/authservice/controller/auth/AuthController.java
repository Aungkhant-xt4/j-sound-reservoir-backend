package org.java.authservice.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.java.authservice.model.dto.LoginRequest;
import org.java.authservice.model.dto.RegisterRequest;
import org.java.authservice.model.response.EmailCheckResponse;
import org.java.authservice.model.response.TokenResponse;
import org.java.authservice.service.auth.AuthService;
import org.java.commonlibrary.model.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        TokenResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> registerUser(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created successfully. You can now log in."));
    }
    @GetMapping("/check-email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<EmailCheckResponse>> checkEmail(@RequestParam String email) {
        boolean exists = authService.checkEmailExists(email);

        return ResponseEntity.ok(
                ApiResponse.success(new EmailCheckResponse(exists))
        );
    }
}