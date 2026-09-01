package com.alvaro.taskmanager.security;

/**
 * Respuesta devuelta al cliente después de un inicio de sesión correcto.
 */
public class AuthResponse {

    private final String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
