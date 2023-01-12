package ru.eliseev.WeatherApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.eliseev.WeatherApp.dto.MeasurementDTO;
import ru.eliseev.WeatherApp.model.Measurement;
import ru.eliseev.WeatherApp.service.MeasurementsService;
import ru.eliseev.WeatherApp.util.vallidation.MeasurementErrorResponse;
import ru.eliseev.WeatherApp.util.vallidation.MeasurementDtoValidator;
import ru.eliseev.WeatherApp.util.MeasurementMapper;
import ru.eliseev.WeatherApp.util.vallidation.MeasurementNotCreatedException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final MeasurementsService measurementsService;
    private final MeasurementDtoValidator measurementDtoValidator;
    private final MeasurementMapper measurementMapper;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService,
                                  MeasurementDtoValidator measurementDtoValidator,
                                  MeasurementMapper measurementMapper) {
        this.measurementsService = measurementsService;
        this.measurementDtoValidator = measurementDtoValidator;
        this.measurementMapper = measurementMapper;
    }

    @GetMapping()
    public List<MeasurementDTO> getMeasurements() {
        return measurementsService.findAll().stream()
                .map(this::convertToMeasurementDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public HashMap<String, Integer> getRainyDaysCount() {
        HashMap<String, Integer> response = new HashMap<>();
        response.put("rainyDaysCount", measurementsService.countRainyDays());
        return response;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        measurementDtoValidator.validate(measurementDTO, bindingResult);

        if(bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMsg.append(error.getField())
                        .append(" – ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new MeasurementNotCreatedException(errorMsg.toString());
        }

        measurementsService.save(convertToMeasurement(measurementDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementNotCreatedException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return measurementMapper.mapToDTO(measurement);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return measurementMapper.mapToMeasurement(measurementDTO);
    }
}
