package com.petProject.booking.config;

import com.petProject.booking.service.BookOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeAuthenticationProvider;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final BookOidcUserService bookOidcUserService;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository, BookOidcUserService bookOidcUserService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.bookOidcUserService = bookOidcUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) {
        OidcClientInitiatedLogoutSuccessHandler handler = new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
        handler.setPostLogoutRedirectUri("http://localhost:8080/main");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/main", true)
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.oidcUserService(this.bookOidcUserService))
                )
                .logout(l -> l.logoutSuccessHandler(handler));
        return http.build();
    }
}
