package com.hvs.ws.back.infra.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@Profile("prod")
class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(registry -> registry
                .requestMatchers("/swagger-ui/**",
                        "/bus/v3/api-docs/**",
                        "/v3/api-docs/**",
                        "/tv-api-docs/**",
                        "/tv-documentation/**")
                        .permitAll())
                .authorizeHttpRequests(registry -> registry
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/propaganda",
                        "/api/v1/propaganda/**",
                        "/api/v1/anuncio",
                        "/api/v1/arquivo/**",
                        "/api/v1/grade/**",
                        "/api/v1/bloco/**",
                        "/api/v1/episodio/**",
                        "/api/v1/programa/**",
                        "/api/v1/canal/**",
                        "/api/v1/genero/**",
                        "/api/v1/linha-vermelha/**")
                        .permitAll())
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/api/v1/**")
                        .hasRole("admin")
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oauth2Configurer -> oauth2Configurer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwt -> {
                                    Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
                                    Collection<String> roles = realmAccess.get("roles");
                                    var grantedAuthorities = roles.stream()
                                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                            .collect(Collectors.toList());
                                    return new JwtAuthenticationToken(jwt, grantedAuthorities);
                }))).build();
    }
}