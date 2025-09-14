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

            // Criando 1000 registros de sensores com valores realistas e maior variação
            List<Sensor> sensores = new ArrayList<>();
            
            // Cenários diferentes para simular condições reais
            String[] cenarios = {
                "ambiente_estavel",      // 30% - Ambiente estável
                "operacao_normal",       // 25% - Operação normal com pequenas variações
                "vibracao_moderada",     // 20% - Vibração moderada (equipamento em movimento)
                "pressao_alta",          // 10% - Pressão mais alta (sistema pressurizado)
                "temperatura_variada",   // 10% - Variações de temperatura (dia/noite)
                "anomalia_menor",        // 5% - Anomalias menores mas realistas
            };
            
            for (int i = 0; i < 1000; i++) {
                // Selecionar cenário baseado na distribuição
                String cenario = selecionarCenario();
                
                // Valores base para cada cenário
                double pressao01Base, pressao02Base, temperaturaBase, vibracaoBase;
                double variacaoPressao01, variacaoPressao02, variacaoTemperatura, variacaoVibracao;
                
                switch (cenario) {
                    case "ambiente_estavel":
                        pressao01Base = 1.01; pressao02Base = 0.101; temperaturaBase = 22.0; vibracaoBase = 0.0;
                        variacaoPressao01 = 0.02; variacaoPressao02 = 0.005; variacaoTemperatura = 0.1; variacaoVibracao = 0.01;
                        break;
                    case "operacao_normal":
                        pressao01Base = 1.02; pressao02Base = 0.102; temperaturaBase = 23.5; vibracaoBase = 0.0;
                        variacaoPressao01 = 0.05; variacaoPressao02 = 0.01; variacaoTemperatura = 0.3; variacaoVibracao = 0.03;
                        break;
                    case "vibracao_moderada":
                        pressao01Base = 1.03; pressao02Base = 0.103; temperaturaBase = 24.0; vibracaoBase = 0.02;
                        variacaoPressao01 = 0.08; variacaoPressao02 = 0.015; variacaoTemperatura = 0.5; variacaoVibracao = 0.08;
                        break;
                    case "pressao_alta":
                        pressao01Base = 2.5; pressao02Base = 0.25; temperaturaBase = 26.0; vibracaoBase = 0.01;
                        variacaoPressao01 = 0.15; variacaoPressao02 = 0.02; variacaoTemperatura = 0.4; variacaoVibracao = 0.05;
                        break;
                    case "temperatura_variada":
                        pressao01Base = 1.01; pressao02Base = 0.101; temperaturaBase = 25.0; vibracaoBase = 0.0;
                        variacaoPressao01 = 0.06; variacaoPressao02 = 0.012; variacaoTemperatura = 1.5; variacaoVibracao = 0.04;
                        break;
                    case "anomalia_menor":
                        pressao01Base = 1.05; pressao02Base = 0.108; temperaturaBase = 24.5; vibracaoBase = 0.03;
                        variacaoPressao01 = 0.12; variacaoPressao02 = 0.025; variacaoTemperatura = 0.8; variacaoVibracao = 0.12;
                        break;
                    default:
                        pressao01Base = 1.01; pressao02Base = 0.101; temperaturaBase = 22.44; vibracaoBase = 0.0;
                        variacaoPressao01 = 0.05; variacaoPressao02 = 0.01; variacaoTemperatura = 0.2; variacaoVibracao = 0.05;
                }
                
                // Gerar valores aleatórios dentro das faixas do cenário
                double pressao01 = pressao01Base + (Math.random() - 0.5) * 2 * variacaoPressao01;
                double pressao02 = pressao02Base + (Math.random() - 0.5) * 2 * variacaoPressao02;
                double temperatura = temperaturaBase + (Math.random() - 0.5) * 2 * variacaoTemperatura;
                
                // Chave fim de curso: variação baseada no cenário
                boolean chaveFimDeCurso;
                if (cenario.equals("operacao_normal") || cenario.equals("vibracao_moderada")) {
                    chaveFimDeCurso = Math.random() < 0.8; // Mais ativa durante operação
                } else if (cenario.equals("anomalia_menor")) {
                    chaveFimDeCurso = Math.random() < 0.4; // Menos ativa durante anomalias
                } else {
                    chaveFimDeCurso = Math.random() < 0.7; // Normal
                }
                
                // Vibrações: variação baseada no cenário
                double vibracaoX = vibracaoBase + (Math.random() - 0.5) * 2 * variacaoVibracao;
                double vibracaoY = vibracaoBase + (Math.random() - 0.5) * 2 * variacaoVibracao;
                double vibracaoZ = vibracaoBase + (Math.random() - 0.5) * 2 * variacaoVibracao;
                
                // Adicionar correlações realistas entre sensores
                if (cenario.equals("temperatura_variada")) {
                    // Temperatura alta pode afetar pressão
                    if (temperatura > temperaturaBase + 0.5) {
                        pressao01 += 0.02;
                        pressao02 += 0.002;
                    }
                }
                
                if (cenario.equals("vibracao_moderada")) {
                    // Vibração pode afetar leituras de pressão
                    double fatorVibracao = Math.sqrt(vibracaoX*vibracaoX + vibracaoY*vibracaoY + vibracaoZ*vibracaoZ);
                    pressao01 += fatorVibracao * 0.01;
                    pressao02 += fatorVibracao * 0.001;
                }
                
                // Garantir que os valores estão dentro dos limites físicos
                pressao01 = Math.max(0, Math.min(7.0, pressao01)); // 0-7 bar
                pressao02 = Math.max(0, Math.min(0.4, pressao02)); // 0-0.4 bar
                temperatura = Math.max(-55, Math.min(125, temperatura)); // -55 a 125°C
                
                sensores.add(criarSensor(pressao01, pressao02, temperatura, chaveFimDeCurso, 
                                       vibracaoX, vibracaoY, vibracaoZ));
            }

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

    private String selecionarCenario() {
        double random = Math.random();
        if (random < 0.30) return "ambiente_estavel";      // 30%
        if (random < 0.55) return "operacao_normal";       // 25%
        if (random < 0.75) return "vibracao_moderada";     // 20%
        if (random < 0.85) return "pressao_alta";          // 10%
        if (random < 0.95) return "temperatura_variada";   // 10%
        return "anomalia_menor";                           // 5%
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
