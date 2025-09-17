package com.fiap.eca.service.api;

import com.fiap.eca.model.Reading;
import java.util.List;

public interface ReadingService {
    Reading saveReading(Reading reading);
    List<Reading> getAllReadings();
    List<Reading> getReadingsBySensorId(String sensorId);
}


