package com.fiap.eca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String email;
    
    private String nome;
    private String senha;
    private String role; // ROLE_USER, ROLE_ADMIN

    public String getSenha() {
        return this.senha;
    }
}