package com.fiap.eca.config;

import com.fiap.eca.repository.SensorRepository;
import com.fiap.eca.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, SensorRepository sensorRepository) {
        return args -> {
            // Banco de dados iniciará vazio - dados serão criados apenas via API
            // Sem dados de teste em produção
        };
    }
}


