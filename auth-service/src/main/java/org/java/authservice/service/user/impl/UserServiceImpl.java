package org.java.authservice.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.java.authservice.model.dto.FirstAndLastNameDto;
import org.java.authservice.repository.UserRepository;
import org.java.authservice.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public String getUsernamebyEmail(String email) {

        FirstAndLastNameDto firstAndLastNameDto = userRepository.findFirstAndLastNameByEmail(email);
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append(firstAndLastNameDto.getFirstName()).append(" ").append(firstAndLastNameDto.getLastName()).toString();
    }

    @Override
    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email))
                .getId();
    }
}
