package org.java.authservice.model.dto;

public record LoginRequest(
        String email,
        String password)
{}

