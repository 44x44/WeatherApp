package ru.eliseev.WeatherApp.util.vallidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.eliseev.WeatherApp.dto.SensorDTO;
import ru.eliseev.WeatherApp.service.SensorsService;

@Component
public class SensorDtoValidator implements Validator {

    private final SensorsService sensorsService;

    @Autowired
    public SensorDtoValidator(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SensorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensor = (SensorDTO) target;

        if (sensorsService.findByName(sensor.getName()).isPresent()) {
            errors.rejectValue("name","", "Sensor with this name already exists");
        }
    }
}
