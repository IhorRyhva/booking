package com.petProject.booking.user;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User addUser(OidcUser user) {
        Optional<User> userOptional = this.repository.findByEmail(user.getEmail());
        return userOptional.orElseGet(() -> repository.save(
                User.builder()
                        .userName(user.getFullName())
                        .email(user.getEmail())
                        .build()
        ));
    }

    public Optional<User> getUser (String email) {
        return this.repository.findByEmail(email);
    }

    public void ban(String email) {
        Optional<User> userOptional = this.getUser(email);
        userOptional.ifPresent(user -> {
            user.setBanned(true);
            System.out.println(user.isBanned());
            this.repository.save(user);
        });
    }

}
