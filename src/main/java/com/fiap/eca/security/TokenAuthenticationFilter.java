package com.fiap.eca.security;

import com.fiap.eca.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public TokenAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = recuperarToken(request);
        
        if (token != null && tokenService.isTokenValid(token)) {
            autenticarCliente(token);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }
        
        return token.substring(7);
    }

    private void autenticarCliente(String token) {
        Long usuarioId = tokenService.getUserId(token);
        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(usuarioId, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
} 