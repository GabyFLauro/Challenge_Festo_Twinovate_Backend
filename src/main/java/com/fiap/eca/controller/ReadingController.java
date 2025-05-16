package com.fiap.eca.controller;

import com.fiap.eca.model.Reading;
import com.fiap.eca.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
@CrossOrigin(origins = "*")
public class ReadingController {
    
    @Autowired
    private ReadingService readingService;
    
    @PostMapping
    public ResponseEntity<Reading> createReading(@RequestBody Reading reading) {
        Reading savedReading = readingService.saveReading(reading);
        return ResponseEntity.ok(savedReading);
    }
    
    @GetMapping
    public ResponseEntity<List<Reading>> getAllReadings() {
        return ResponseEntity.ok(readingService.getAllReadings());
    }
    
    @GetMapping("/{sensorId}")
    public ResponseEntity<List<Reading>> getReadingsBySensorId(@PathVariable String sensorId) {
        return ResponseEntity.ok(readingService.getReadingsBySensorId(sensorId));
    }
} 