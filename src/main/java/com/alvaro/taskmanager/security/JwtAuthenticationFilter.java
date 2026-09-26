package com.alvaro.taskmanager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene la cabecera Authorization
        String authHeader = request.getHeader("Authorization");

        // Si no existe o no empieza por Bearer, continúa sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el token eliminando "Bearer "
        String token = authHeader.substring(7);

        // Obtiene el username guardado dentro del JWT
        String username = jwtService.extractUsername(token);

        // Crea la autenticación para Spring Security
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList()
                );

        // Guarda la autenticación en el contexto de seguridad
        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        // Continúa con la petición
        filterChain.doFilter(request, response);
    }
}