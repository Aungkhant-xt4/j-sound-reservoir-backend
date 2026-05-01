package org.java.commonlibrary.service.jwt;


public interface JwtService {
    String generateToken(String username, String email, String role);
    String extractUsername(String token);
    boolean isTokenValid(String token, String username);
}