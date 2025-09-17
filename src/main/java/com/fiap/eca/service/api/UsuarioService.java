package com.fiap.eca.service.api;

import com.fiap.eca.model.Usuario;

import java.util.Optional;

public interface UsuarioService {
    Usuario cadastrar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Usuario atualizar(Long id, Usuario usuario);
    void deletar(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    Usuario alterarSenha(Long id, String novaSenha);
}


