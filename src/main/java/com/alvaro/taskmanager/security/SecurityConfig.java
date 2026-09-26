package com.alvaro.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configura las reglas de acceso de Spring Security.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public JwtFilter jwtFilter(JwtService jwtService) {
        return new JwtFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtFilter jwtFilter) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/kafka/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}