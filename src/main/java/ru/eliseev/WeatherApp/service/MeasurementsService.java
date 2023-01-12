package ru.eliseev.WeatherApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.eliseev.WeatherApp.repository.MeasurementsRepository;
import ru.eliseev.WeatherApp.repository.SensorsRepository;
import ru.eliseev.WeatherApp.model.Measurement;
import ru.eliseev.WeatherApp.model.Sensor;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementsService {

    private final MeasurementsRepository measurementsRepository;
    private final SensorsRepository sensorsRepository;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorsRepository sensorsRepository) {
        this.measurementsRepository = measurementsRepository;
        this.sensorsRepository = sensorsRepository;
    }

    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    public int countRainyDays() {
        int res = 0;

        for (Measurement measurement : measurementsRepository.findAll()) {
            if (measurement.isRaining()) {
                res++;
            }
        }

        return res;
    }

    @Transactional
    public void save(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementsRepository.save(measurement);
    }

    private void enrichMeasurement(Measurement measurement) {
        Sensor sensor = sensorsRepository.findByName(measurement.getSensor().getName()).get();
        measurement.setSensor(sensor);
        measurement.setTime(new Date());
    }
}
