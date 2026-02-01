package com.petProject.booking.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void addUser(OidcUser user) {
        if (!repository.existsUserByEmail(user.getEmail())) {
            repository.save(
                    User.builder()
                    .userName(user.getFullName())
                    .email(user.getEmail())
                    .role(user.getAuthorities().toString())
                    .build()
            );
        }
    }

    public Optional<User> getUser (String email) {
        return this.repository.findByEmail(email);
    }
}
