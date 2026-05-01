package org.java.authservice.service.user;

public interface UserService {
    String getUsernamebyEmail(String email);
    Long getUserIdByEmail(String email);

}
