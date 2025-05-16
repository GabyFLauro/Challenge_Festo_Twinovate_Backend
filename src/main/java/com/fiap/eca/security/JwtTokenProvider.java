package com.fiap.eca.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key chaveSecreta;

    @Value("${jwt.expiration}")
    private long tempoExpiracao;

    public JwtTokenProvider(@Value("${jwt.secret}") String segredo) {
        this.chaveSecreta = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String gerarToken(String email) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + tempoExpiracao);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(agora)
                .setExpiration(expiracao)
                .signWith(chaveSecreta, SignatureAlgorithm.HS256)
                .compact();
    }

    public String obterEmailDoToken(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey)chaveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey)chaveSecreta)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
} 