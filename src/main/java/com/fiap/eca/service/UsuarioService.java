package com.fiap.eca.service;

import com.fiap.eca.model.Usuario;
import com.fiap.eca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario cadastrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        if (usuario.getRole() == null) {
            usuario.setRole("ROLE_USER");
        }
        
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario atualizar(Long id, Usuario usuario) {
        Optional<Usuario> existingUser = usuarioRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario userToUpdate = existingUser.get();
        userToUpdate.setNome(usuario.getNome());
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            userToUpdate.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        
        return usuarioRepository.save(userToUpdate);
    }

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
} 