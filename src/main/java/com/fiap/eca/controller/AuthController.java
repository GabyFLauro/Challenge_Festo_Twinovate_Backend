package com.fiap.eca.controller;

import com.fiap.eca.dto.LoginDTO;
import com.fiap.eca.dto.TokenDTO;
import com.fiap.eca.model.Usuario;
import com.fiap.eca.service.TokenService;
import com.fiap.eca.service.UsuarioService;
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
    public ResponseEntity<Object> login(@RequestBody @jakarta.validation.Valid LoginDTO loginDTO) {
        Optional<Usuario> usuario = usuarioService.buscarPorEmail(loginDTO.getEmail());
        if (usuario.isPresent()) {
            System.out.println("Tentando login para: " + loginDTO.getEmail());
            System.out.println("Senha digitada: " + loginDTO.getSenha());
            System.out.println("Hash salvo: " + usuario.get().getSenha());
            boolean match = passwordEncoder.matches(loginDTO.getSenha(), usuario.get().getSenha());
            System.out.println("Password match: " + match);
            if (match) {
                String token = tokenService.generateToken(usuario.get());
                return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
            }
        }
        System.out.println("Login falhou para: " + loginDTO.getEmail());
        return ResponseEntity.badRequest().body("Usuário ou senha inválidos");
    }
}