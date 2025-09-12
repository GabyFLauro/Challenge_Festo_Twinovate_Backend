package com.fiap.eca.api_marcacao_consultas.config;

import com.fiap.eca.api_marcacao_consultas.model.Usuario;
import com.fiap.eca.api_marcacao_consultas.model.Sensor;
import com.fiap.eca.api_marcacao_consultas.repository.UsuarioRepository;
import com.fiap.eca.api_marcacao_consultas.repository.SensorRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataInitializer {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PersistenceContext
    private EntityManager entityManager;

    // Nome do arquivo que será usado como flag para indicar que a inicialização já
    // foi feita
    private static final String INIT_FLAG_FILE = "./data/db_initialized.flag";

    @Bean
    @Transactional
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            SensorRepository sensorRepository) {

        return args -> {
            // Verifica se o arquivo de flag existe
            File flagFile = new File(INIT_FLAG_FILE);

            if (flagFile.exists()) {
                System.out.println("Banco de dados já foi inicializado anteriormente. Pulando inicialização.");
                return;
            }

            // Limpar o banco de dados antes de inserir novos dados
            System.out.println("Limpando banco de dados...");
            usuarioRepository.deleteAllInBatch();

            // Resetar sequências de ID (específico para H2)
            try {
                entityManager.createNativeQuery("ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 1").executeUpdate();
            } catch (Exception e) {
                System.out.println(
                        "Aviso: Não foi possível resetar as sequências de ID. Isso é normal na primeira execução.");
            }

            System.out.println("Inicializando banco de dados com dados de exemplo...");

            // Criando usuário admin
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@clinica.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            usuarioRepository.save(admin);
            System.out.println("Administrador criado");

            // Criando usuários comuns de exemplo
            List<Usuario> comuns = new ArrayList<>();
            String[][] dadosComuns = {
                    { "João Pereira", "joao.pereira@email.com", "senha123" },
                    { "Maria Souza", "maria.souza@email.com", "senha123" },
                    { "Pedro Almeida", "pedro.almeida@email.com", "senha123" }
            };

            for (String[] dados : dadosComuns) {
                Usuario comum = new Usuario();
                comum.setNome(dados[0]);
                comum.setEmail(dados[1]);
                comum.setSenha(passwordEncoder.encode(dados[2]));
                comuns.add(comum);
            }

            usuarioRepository.saveAll(comuns);
            System.out.println("Usuários comuns criados: " + comuns.size());

            // Criando dados de sensores realistas
            List<Sensor> sensores = new ArrayList<>();
            sensores.add(criarSensor(101.3, 100.8, 26.4, true, 0.12, 0.08, 0.10));
            sensores.add(criarSensor(102.1, 101.6, 25.9, false, 0.15, 0.11, 0.09));
            sensores.add(criarSensor(99.7,  99.5,  27.2, true, 0.20, 0.14, 0.13));

            sensorRepository.saveAll(sensores);
            System.out.println("Sensores criados: " + sensores.size());

            // Criar o arquivo de flag para indicar que a inicialização foi concluída
            criarArquivoFlag();

            System.out.println("Inicialização de dados concluída com sucesso!");
        };
    }

    private Sensor criarSensor(double p1, double p2, double temp, boolean chave, double vx, double vy, double vz) {
        Sensor s = new Sensor();
        s.setPressao01Xgzp701db1r(p1);
        s.setPressao02Hx710b(p2);
        s.setTemperaturaDs18b20(temp);
        s.setChaveFimDeCurso(chave);
        s.setVibracaoVibX(vx);
        s.setVibracaoVibY(vy);
        s.setVibracaoVibZ(vz);
        return s;
    }

    /**
     * Cria um arquivo de flag para indicar que a inicialização do banco de dados
     * foi concluída
     */
    private void criarArquivoFlag() {
        try {
            // Garantir que o diretório existe
            Path diretorio = Paths.get("./data");
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }

            // Criar o arquivo de flag
            Path flagPath = Paths.get(INIT_FLAG_FILE);
            Files.createFile(flagPath);
            Files.write(flagPath, ("Banco de dados inicializado em: " +
                    java.time.LocalDateTime.now().toString()).getBytes());

            System.out.println("Arquivo de flag criado. O banco não será reinicializado nas próximas execuções.");
        } catch (IOException e) {
            System.err.println("Aviso: Não foi possível criar o arquivo de flag: " + e.getMessage());
            System.err.println("O banco de dados pode ser reinicializado na próxima execução.");
        }
    }
}
