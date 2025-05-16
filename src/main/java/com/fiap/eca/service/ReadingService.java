package com.fiap.eca.service;

import com.fiap.eca.model.Reading;
import com.fiap.eca.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingService {
    
    @Autowired
    private ReadingRepository readingRepository;
    
    public Reading saveReading(Reading reading) {
        return readingRepository.save(reading);
    }
    
    public List<Reading> getAllReadings() {
        return readingRepository.findAll();
    }
    
    public List<Reading> getReadingsBySensorId(String sensorId) {
        return readingRepository.findBySensorId(sensorId);
    }
} 