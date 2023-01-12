package ru.eliseev.WeatherApp.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.eliseev.WeatherApp.dto.SensorDTO;
import ru.eliseev.WeatherApp.model.Sensor;
import ru.eliseev.WeatherApp.service.SensorsService;
import ru.eliseev.WeatherApp.util.vallidation.SensorDtoValidator;
import ru.eliseev.WeatherApp.util.vallidation.SensorErrorResponse;
import ru.eliseev.WeatherApp.util.vallidation.SensorNotCreatedException;

import javax.validation.Valid;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
    private final SensorsService sensorsService;
    private final SensorDtoValidator sensorDtoValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public SensorsController(SensorsService sensorsService, SensorDtoValidator sensorDtoValidator, ModelMapper modelMapper) {
        this.sensorsService = sensorsService;
        this.sensorDtoValidator = sensorDtoValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid SensorDTO sensorDTO,
                                               BindingResult bindingResult) {
        sensorDtoValidator.validate(sensorDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMsg.append(error.getField())
                        .append(" – ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new SensorNotCreatedException(errorMsg.toString());
        }

        sensorsService.save(convertToSensor(sensorDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }
}
