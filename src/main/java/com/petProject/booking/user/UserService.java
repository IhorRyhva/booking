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

    public User getUser(String email, String userName) {
        Optional<User> optionalUser = this.repository.findByEmail(email);
        User user = optionalUser.orElseGet(() -> User.builder()
                .email(email)
                .userName(userName)
                .role("")
                .books(new ArrayList<>())
                .build());
        return user;
    }

    public User createUser(String email, String userName) {
        return this.repository.save(User.builder()
                        .userName(userName)
                        .email(email)
                .build());
    }
}
