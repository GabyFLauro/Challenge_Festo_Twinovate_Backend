package com.fiap.eca.service.api;

import com.fiap.eca.model.Sensor;
import java.util.List;

public interface SensorService {
    List<Sensor> listarSensores();
    Sensor salvarSensor(Sensor sensor);
    Sensor buscarPorId(Long id);
    void deletarSensor(Long id);
}


