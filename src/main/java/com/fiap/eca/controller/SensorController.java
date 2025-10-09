package com.fiap.eca.controller;

import com.fiap.eca.model.Sensor;
import com.fiap.eca.service.api.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class SensorController {
    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/sensores")
    public ResponseEntity<List<Sensor>> listarSensores() {
        return ResponseEntity.ok(sensorService.listarSensores());
    }

    @GetMapping("/sensors")
    public ResponseEntity<List<Sensor>> listarSensorsEnglish() {
        return ResponseEntity.ok(sensorService.listarSensores());
    }
}


