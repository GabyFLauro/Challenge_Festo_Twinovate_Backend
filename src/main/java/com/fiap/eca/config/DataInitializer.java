package com.fiap.eca.config;

import com.fiap.eca.model.Sensor;
import com.fiap.eca.model.Usuario;
import com.fiap.eca.repository.SensorRepository;
import com.fiap.eca.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final String INIT_FLAG_FILE = "./data/db_initialized.flag";

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    @Transactional
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, SensorRepository sensorRepository) {
        return args -> {
            File flagFile = new File(INIT_FLAG_FILE);
            if (flagFile.exists()) {
                return;
            }

            usuarioRepository.deleteAllInBatch();
            try {
                entityManager.createNativeQuery("ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 1").executeUpdate();
            } catch (Exception ignored) { }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@clinica.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            usuarioRepository.save(admin);

            List<Usuario> comuns = new ArrayList<>();
            String[][] dadosComuns = {
                    {"João Pereira", "joao.pereira@email.com", "senha123"},
                    {"Maria Souza", "maria.souza@email.com", "senha123"},
                    {"Pedro Almeida", "pedro.almeida@email.com", "senha123"}
            };
            for (String[] dados : dadosComuns) {
                Usuario comum = new Usuario();
                comum.setNome(dados[0]);
                comum.setEmail(dados[1]);
                comum.setSenha(passwordEncoder.encode(dados[2]));
                comum.setRole("ROLE_USER");
                comuns.add(comum);
            }
            usuarioRepository.saveAll(comuns);

            List<Sensor> sensores = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                double pressao01 = 1.01 + (Math.random() - 0.5) * 0.1;
                double pressao02 = 0.101 + (Math.random() - 0.5) * 0.01;
                double temperatura = 24.0 + (Math.random() - 0.5) * 2.0;
                boolean chaveFim = Math.random() < 0.7;
                double vx = (Math.random() - 0.5) * 0.1;
                double vy = (Math.random() - 0.5) * 0.1;
                double vz = (Math.random() - 0.5) * 0.1;
                Sensor s = new Sensor();
                s.setPressao01Xgzp701db1r(pressao01);
                s.setPressao02Hx710b(pressao02);
                s.setTemperaturaDs18b20(temperatura);
                s.setChaveFimDeCurso(chaveFim);
                s.setVibracaoVibX(vx);
                s.setVibracaoVibY(vy);
                s.setVibracaoVibZ(vz);
                sensores.add(s);
            }
            sensorRepository.saveAll(sensores);

            criarArquivoFlag();
        };
    }

    private void criarArquivoFlag() {
        try {
            Path diretorio = Paths.get("./data");
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }
            Path flagPath = Paths.get(INIT_FLAG_FILE);
            Files.createFile(flagPath);
            Files.write(flagPath, ("Banco de dados inicializado").getBytes());
        } catch (IOException ignored) { }
    }
}


