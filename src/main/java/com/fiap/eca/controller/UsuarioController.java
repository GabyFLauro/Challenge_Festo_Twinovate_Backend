package com.fiap.eca.controller;

import com.fiap.eca.model.Usuario;
import com.fiap.eca.dto.UsuarioRequest;
import com.fiap.eca.dto.UsuarioResponse;
import com.fiap.eca.service.api.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @jakarta.validation.Valid UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        Usuario novoUsuario = usuarioService.cadastrar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioResponse(novoUsuario.getId(), novoUsuario.getNome(), novoUsuario.getEmail(), novoUsuario.getRole()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(u -> ResponseEntity.ok(new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getRole())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody @jakarta.validation.Valid UsuarioRequest request) {
        try {
            Usuario updates = new Usuario();
            updates.setNome(request.getNome());
            updates.setSenha(request.getSenha());
            Usuario usuarioAtualizado = usuarioService.atualizar(id, updates);
            return ResponseEntity.ok(new UsuarioResponse(usuarioAtualizado.getId(), usuarioAtualizado.getNome(), usuarioAtualizado.getEmail(), usuarioAtualizado.getRole()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            usuarioService.deletar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para admin alterar senha de qualquer usuário
     */
    @PutMapping("/{id}/senha")
    public ResponseEntity<Object> alterarSenhaUsuario(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String novaSenha = request.get("novaSenha");
            if (novaSenha == null || novaSenha.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nova senha é obrigatória");
            }

            Usuario usuario = usuarioService.alterarSenha(id, novaSenha);
            return ResponseEntity.ok().body(Map.of("message", "Senha alterada com sucesso", "usuario", usuario.getNome()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}