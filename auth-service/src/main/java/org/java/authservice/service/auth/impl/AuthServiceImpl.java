package org.java.authservice.service.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.java.authservice.model.entity.Role;
import org.java.authservice.model.entity.User;
import org.java.authservice.model.dto.LoginRequest;
import org.java.authservice.model.dto.RegisterRequest;
import org.java.authservice.model.response.TokenResponse;
import org.java.authservice.repository.RoleRepository;
import org.java.authservice.repository.UserRepository;
import org.java.authservice.service.auth.AuthService;
import org.java.commonlibrary.service.jwt.JwtService;
import org.java.authservice.service.user.UserService;
import org.java.commonlibrary.exception.BadRequestException;
import org.java.commonlibrary.exception.ResourceNotFoundException;
import org.java.commonlibrary.exception.UnauthorizedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("An account with this email already exists.");
        }
        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role 'ROLE_USER' not found in database."));
        User newUser = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .residenceCountry(request.residenceCountry())
                .guid(UUID.randomUUID().toString())
                .roles(Set.of(userRole))
                .build();
        userRepository.save(newUser);
        log.info("Successfully registered new user with email: {}", request.email());
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            String role = authentication.getAuthorities().iterator().next().getAuthority();
            String username = userService.getUsernamebyEmail(request.email());

            log.info("User {}", username);
            String token = jwtService.generateToken(username, authentication.getName(), role);

            log.info("User {} logged in successfully", request.email());
            return new TokenResponse(token);
        } catch (Exception e){
            throw new UnauthorizedException(e.getMessage());
        }

    }

    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}