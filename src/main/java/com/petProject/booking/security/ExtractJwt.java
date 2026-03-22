package com.petProject.booking.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;

@Configuration
public class ExtractJwt {

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private  String issuer;

    @Bean
    public JwtDecoder getDecoder() {
        return JwtDecoders.fromIssuerLocation(issuer);
    }
}
