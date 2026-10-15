package com.alvaro.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de la API.
 *
 * <p>La autenticación se basa en tokens JWT: cada petición protegida debe
 * incluir la cabecera {@code Authorization: Bearer <token>}. El filtro
 * {@link JwtFilter} valida ese token y deja el usuario autenticado en el
 * contexto de Spring Security.</p>
 */
@Configuration
public class SecurityConfig {

    private final JwtService jwtService;

    /**
     * Inyecta el servicio JWT necesario para construir el filtro.
     *
     * @param jwtService servicio que genera y lee los tokens
     */
    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Define la cadena de filtros y las reglas de autorización de la API.
     *
     * @param http configurador de seguridad HTTP de Spring Security
     * @return cadena de filtros configurada
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // Las APIs REST sin estado no necesitan protección CSRF:
                // el cliente se autentica con un token JWT, no con una
                // sesión de navegador.
                .csrf(csrf -> csrf.disable())

                // La aplicación es stateless: el servidor no crea sesiones
                // HTTP, el "estado" viaja dentro del propio token JWT.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Registra el filtro JWT justo antes del filtro de
                // autenticación por formulario de Spring Security, de modo
                // que la petición ya esté autenticada cuando se evalúen
                // las reglas de autorización.
                .addFilterBefore(new JwtFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class)

                // Reglas de autorización de las rutas.
                .authorizeHttpRequests(auth -> auth
                        // /auth/** es público: el login es el punto de
                        // entrada donde el cliente obtiene su token JWT.
                        .requestMatchers("/auth/**").permitAll()
                        // Cualquier otra ruta (tareas, Kafka, ...) exige
                        // un usuario autenticado, es decir, un JWT válido.
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
