package com.fiap.eca.controller;

import com.fiap.eca.dto.LoginDTO;
import com.fiap.eca.dto.TokenDTO;
import com.fiap.eca.model.Usuario;
import com.fiap.eca.service.TokenService;
import com.fiap.eca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @jakarta.validation.Valid LoginDTO loginDTO) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(loginDTO.getEmail());
        
        if (usuario.isPresent() && passwordEncoder.matches(loginDTO.getSenha(), usuario.get().getSenha())) {
            String token = tokenService.generateToken(usuario.get());
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        }
        
        return ResponseEntity.badRequest().body("Usuário ou senha inválidos");
    }
} 