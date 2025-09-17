package com.fiap.eca.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sensores")
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double pressao01Xgzp701db1r;
    private Double pressao02Hx710b;
    private Double temperaturaDs18b20;
    private Boolean chaveFimDeCurso;
    private Double vibracaoVibX;
    private Double vibracaoVibY;
    private Double vibracaoVibZ;
}


