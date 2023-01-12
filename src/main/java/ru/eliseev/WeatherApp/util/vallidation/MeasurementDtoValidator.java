package ru.eliseev.WeatherApp.util.vallidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.eliseev.WeatherApp.dto.MeasurementDTO;
import ru.eliseev.WeatherApp.service.SensorsService;

@Component
public class MeasurementDtoValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public MeasurementDtoValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeasurementDTO measurementDTO = (MeasurementDTO) target;

        if (measurementDTO.getSensor() != null &&
                sensorsService.findByName(measurementDTO.getSensor().getName()).isEmpty()) {
            errors.rejectValue("sensor", "", "Sensor with this name does not exist");
        }
    }
}
