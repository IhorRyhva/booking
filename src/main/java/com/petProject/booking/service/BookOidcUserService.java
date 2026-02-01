package com.petProject.booking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookOidcUserService extends OidcUserService {

    private final JwtDecoder jwtDecoder;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    public BookOidcUserService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }


    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser defaultUser = super.loadUser(userRequest);

        String token = userRequest.getAccessToken().getTokenValue();
        Set<GrantedAuthority> grantedAuthorities = extractRole(token);
        grantedAuthorities.add(new SimpleGrantedAuthority("user"));

        grantedAuthorities.addAll(defaultUser.getAuthorities());
        return new DefaultOidcUser(grantedAuthorities, defaultUser.getIdToken(), defaultUser.getUserInfo());
    }

    private Set<GrantedAuthority> extractRole(String token) {
        Jwt jwt = this.jwtDecoder.decode(token);
        Map<String, Object> resources = jwt.getClaim("resource_access");

        if (resources.isEmpty()) {
            return new HashSet<>();
        }

        Map<String, Object> clientResources = (Map<String, Object>) resources.get(this.clientId);

        if (clientResources == null || clientResources.isEmpty()) {
            return new HashSet<>();
        }

        ArrayList<String> roles = (ArrayList<String>) clientResources.get("roles");

        if (roles.isEmpty()) {
            return new HashSet<>();
        }

        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
