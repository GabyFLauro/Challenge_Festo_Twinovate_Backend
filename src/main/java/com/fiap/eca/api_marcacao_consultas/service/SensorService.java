package com.fiap.eca.api_marcacao_consultas.service;

import com.fiap.eca.api_marcacao_consultas.model.Sensor;
import com.fiap.eca.api_marcacao_consultas.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    public List<Sensor> listarSensores() {
        return sensorRepository.findAll();
    }

    public Sensor salvarSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    public Sensor buscarPorId(Long id) {
        return sensorRepository.findById(id).orElse(null);
    }

    public void deletarSensor(Long id) {
        sensorRepository.deleteById(id);
    }
}
