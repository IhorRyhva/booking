package com.petProject.booking.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final BookOidcUserService bookOidcUserService;

    @Value("${spring.application.logout-url}")
    private String logoutUrl;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository, BookOidcUserService bookOidcUserService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.bookOidcUserService = bookOidcUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) {
        OidcClientInitiatedLogoutSuccessHandler handler = new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);
        handler.setPostLogoutRedirectUri(this.logoutUrl);

        http
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .defaultSuccessUrl("/main", true)
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.oidcUserService(this.bookOidcUserService))
                ).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasAuthority("admin")
                        .requestMatchers("/bookedRoom").authenticated()
                        .requestMatchers("/bookRoom", "/main", "/login", "/result").permitAll()
                        .anyRequest().authenticated())
                .logout(l -> l.logoutSuccessHandler(handler));
        return http.build();
    }
}
