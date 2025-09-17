package com.fiap.eca.service;

import com.fiap.eca.model.Usuario;
import com.fiap.eca.repository.UsuarioRepository;
import com.fiap.eca.service.api.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
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

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
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

    @Override
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuario alterarSenha(Long id, String novaSenha) {
        Optional<Usuario> existingUser = usuarioRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario userToUpdate = existingUser.get();
        userToUpdate.setSenha(passwordEncoder.encode(novaSenha));
        return usuarioRepository.save(userToUpdate);
    }
}


