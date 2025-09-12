package com.fiap.eca.api_marcacao_consultas.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sensores")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pressão 01 Xgzp701db1r (kPa)
    private Double pressao01Xgzp701db1r;

    // Pressão 02 hx710b (kPa)
    private Double pressao02Hx710b;

    // Temperatura Ds18b20 (°C)
    private Double temperaturaDs18b20;

    // Chave fim de curso (booleano - 0/1)
    private Boolean chaveFimDeCurso;

    // Vibrações (m/s^2 ou g, usar unidade consistente)
    private Double vibracaoVibX;
    private Double vibracaoVibY;
    private Double vibracaoVibZ;
}
