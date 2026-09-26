package com.alvaro.taskmanager.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Servicio encargado de generar y leer tokens JWT.
 */
@Service
public class JwtService {

    // Clave utilizada para firmar y validar los tokens. Debe mantenerse secreta. la podria meter en RESOURCES TAMBIEN
    private static final String SECRET_KEY =
            "123456789123456789123456789123456789123456789";

    // Duración del token: una hora expresada en milisegundos.
    private static final long TOKEN_EXPIRATION_MILLIS = 1000L * 60 * 60;

    /**
     * Genera un token JWT para el usuario indicado.
     *
     * @param username nombre del usuario que se incluirá en el token
     * @return token JWT firmado
     */
    public String generateToken(String username) {
        Date issuedAt = new Date();
        Date expiration = new Date(
                issuedAt.getTime() + TOKEN_EXPIRATION_MILLIS
        );

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el nombre de usuario de un token válido.
     *
     * @param token token JWT firmado
     * @return nombre de usuario almacenado como subject
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Construye la clave criptográfica a partir de la clave configurada.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }
}
