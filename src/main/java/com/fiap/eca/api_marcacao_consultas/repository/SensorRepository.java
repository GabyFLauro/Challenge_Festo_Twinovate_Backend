package com.fiap.eca.api_marcacao_consultas.repository;

import com.fiap.eca.api_marcacao_consultas.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
