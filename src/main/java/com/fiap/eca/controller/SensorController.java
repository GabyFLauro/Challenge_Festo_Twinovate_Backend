package com.fiap.eca.controller;

import com.fiap.eca.model.Sensor;
import com.fiap.eca.service.api.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sensores")
public class SensorController {
    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    public ResponseEntity<List<Sensor>> listar() {
        return ResponseEntity.ok(sensorService.listarSensores());
    }
}


