package br.com.atom.api_app_csc.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Chave gerada aleatoriamente

    // Método para gerar um token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Define o usuário
                .setIssuedAt(new Date()) // Data de criação
                .signWith(SECRET_KEY) // Assinatura com chave secreta
                .compact();
    }

    // Método para validar o token
    public static boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extrai o nome do usuário do token
    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Verifica se o token expirou
    public static boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Extrai todas as informações do token
    private static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}