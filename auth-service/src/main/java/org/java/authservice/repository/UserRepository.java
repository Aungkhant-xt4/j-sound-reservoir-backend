package org.java.authservice.repository;

import org.java.authservice.model.entity.User;
import org.java.authservice.model.dto.FirstAndLastNameDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = """
        SELECT first_name, last_name
        FROM users
        WHERE email = :email
        """, nativeQuery = true)
    FirstAndLastNameDto findFirstAndLastNameByEmail(@Param("email") String email);

}
