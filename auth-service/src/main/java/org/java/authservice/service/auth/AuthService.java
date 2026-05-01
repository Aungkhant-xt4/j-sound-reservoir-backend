package org.java.authservice.service.auth;

import org.java.authservice.model.dto.LoginRequest;
import org.java.authservice.model.dto.RegisterRequest;
import org.java.authservice.model.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
    void register(RegisterRequest request);
    boolean checkEmailExists(String email);

}