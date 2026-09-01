package com.alvaro.taskmanager.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que autentica usuarios y entrega tokens JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String VALID_USERNAME = "alvaro";
    private static final String VALID_PASSWORD = "1234";

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Endpoint de prueba.
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {          // ← faltaba la llave {
        return ResponseEntity.ok("FUNCIONA");
    }

    /**
     * Comprueba las credenciales y genera un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(      // ← sobraba el ...
                                                    @RequestBody LoginRequest request) {

        if (request == null
                || !VALID_USERNAME.equals(request.getUsername())
                || !VALID_PASSWORD.equals(request.getPassword())) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        String token = jwtService.generateToken(request.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}