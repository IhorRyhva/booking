package com.petProject.booking.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.Set;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookOidcUserServiceTest {
    @Mock
    JwtDecoder jwtDecoder;

    @Test
    public void returnEmptySetWithEmptyResources() {
        when(jwtDecoder.decode(any())).thenReturn(Jwt
                .withTokenValue("aeeeeeaq")
                .claim("sub", "oh")
                .headers(header -> header.put("key", "value"))
                .build());
        BookOidcUserService oidcUserService = new BookOidcUserService(jwtDecoder);
        Set<GrantedAuthority> result = oidcUserService.extractRole("3wrgchd");
        assertThat(result).isEmpty();
    }
}
