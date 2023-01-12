package ru.eliseev.WeatherApp.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.eliseev.WeatherApp.dto.MeasurementDTO;
import ru.eliseev.WeatherApp.dto.SensorDTO;
import ru.eliseev.WeatherApp.model.Measurement;
import ru.eliseev.WeatherApp.service.SensorsService;

@Component
public class MeasurementMapper {
    private final ModelMapper modelMapper;
    private final SensorsService sensorsService;

    @Autowired
    public MeasurementMapper(ModelMapper modelMapper, SensorsService sensorsService) {
        this.modelMapper = modelMapper;
        this.sensorsService = sensorsService;
    }

    public Measurement mapToMeasurement(MeasurementDTO measurementDTO) {
        Measurement measurement = new Measurement();

        measurement.setValue(measurementDTO.getValue());
        measurement.setRaining(measurementDTO.isRaining());
        measurement.setSensor(sensorsService.findByName(measurementDTO.getSensor().getName()).get());

        return measurement;
    }

    public MeasurementDTO mapToDTO(Measurement measurement) {
        MeasurementDTO measurementDTO = new MeasurementDTO();

        measurementDTO.setValue(measurement.getValue());
        measurementDTO.setRaining(measurement.isRaining());
        measurementDTO.setSensor(modelMapper.map(measurement.getSensor(), SensorDTO.class));

        return measurementDTO;
    }
}
