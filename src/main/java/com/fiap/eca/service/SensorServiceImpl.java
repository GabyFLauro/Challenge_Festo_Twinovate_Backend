package com.fiap.eca.service;

import com.fiap.eca.model.Sensor;
import com.fiap.eca.repository.SensorRepository;
import com.fiap.eca.service.api.SensorService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;

    public SensorServiceImpl(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public List<Sensor> listarSensores() {
        return sensorRepository.findAll();
    }

    @Override
    public Sensor salvarSensor(Sensor sensor) {
        return sensorRepository.save(sensor);
    }

    @Override
    public Sensor buscarPorId(Long id) {
        return sensorRepository.findById(id).orElse(null);
    }

    @Override
    public void deletarSensor(Long id) {
        sensorRepository.deleteById(id);
    }
}


