package com.fiap.eca.service;

import com.fiap.eca.model.Reading;
import com.fiap.eca.repository.ReadingRepository;
import com.fiap.eca.service.api.ReadingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingServiceImpl implements ReadingService {

    private final ReadingRepository readingRepository;

    public ReadingServiceImpl(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    @Override
    public Reading saveReading(Reading reading) {
        return readingRepository.save(reading);
    }

    @Override
    public List<Reading> getAllReadings() {
        return readingRepository.findAll();
    }

    @Override
    public List<Reading> getReadingsBySensorId(String sensorId) {
        return readingRepository.findBySensorId(sensorId);
    }
}


