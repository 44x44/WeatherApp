package ru.eliseev.WeatherApp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.eliseev.WeatherApp.dto.MeasurementDTO;
import ru.eliseev.WeatherApp.dto.SensorDTO;
import ru.eliseev.WeatherApp.model.Measurement;
import ru.eliseev.WeatherApp.service.MeasurementsService;
import ru.eliseev.WeatherApp.util.vallidation.MeasurementDtoValidator;
import ru.eliseev.WeatherApp.util.MeasurementMapper;

import java.util.Collections;
import java.util.Random;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MeasurementsController.class)
class MeasurementsControllerTest {

    @MockBean
    private MeasurementsService measurementsService;

    @MockBean
    private MeasurementDtoValidator measurementDtoValidator;

    @MockBean
    private MeasurementMapper measurementMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMeasurements_whenMeasurementsExist_shouldReturnMeasurements() throws Exception {
        MeasurementDTO measurementSample = new MeasurementDTO(
                24.7f,
                false,
                new SensorDTO("Test sensor")
        );

        doReturn(measurementSample)
                .when(measurementMapper)
                .mapToDTO(isA(Measurement.class));

        doReturn(Collections.singletonList(new Measurement()))
                .when(measurementsService)
                .findAll();

        mockMvc.perform(get("/measurements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(
                        "[{" +
                                    "\"value\":24.7," +
                                    "\"raining\":false," +
                                    "\"sensor\":{" +
                                        "\"name\":\"Test sensor\"" +
                                    "}" +
                                "}]"));

        verify(measurementsService, times(1))
                .findAll();

        verify(measurementMapper, times(1))
                .mapToDTO(isA(Measurement.class));
    }

    @Test
    void getMeasurements_whenNoMeasurements_shouldReturnEmptyList() throws Exception {
        doReturn(Collections.emptyList())
                .when(measurementsService)
                .findAll();

        mockMvc.perform(get("/measurements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("[]"));

        verify(measurementsService, times(1))
                .findAll();
    }

    @Test
    void getRainyDaysCount_always_shouldReturnCount() throws Exception {
        Random random = new Random();
        int rainyDaysCount = random.nextInt();

        doReturn(rainyDaysCount)
                .when(measurementsService)
                .countRainyDays();

        mockMvc.perform(get("/measurements/rainyDaysCount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("{\"rainyDaysCount\":" + rainyDaysCount + "}"));

        verify(measurementsService, times(1))
                .countRainyDays();
    }

    @Test
    void addMeasurement_whenValidatedAndSaved_shouldReturnStatus200() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO(
                24.7f,
                false,
                new SensorDTO("Test sensor")
        );

        doNothing()
                .when(measurementDtoValidator)
                .validate(isA(MeasurementDTO.class), isA(BindingResult.class));

        doReturn(new Measurement())
                .when(measurementMapper)
                .mapToMeasurement(isA(MeasurementDTO.class));

        doNothing()
                .when(measurementsService)
                .save(isA(Measurement.class));

        mockMvc.perform(post("/measurements/add")
                        .contentType("application/json")
                        .content(JsonMapper.mapToJson(measurementDTO)))
                .andExpect(status().isOk());

        verify(measurementDtoValidator, times(1))
                .validate(isA(MeasurementDTO.class), isA(BindingResult.class));

        verify(measurementMapper, times(1))
                .mapToMeasurement(isA(MeasurementDTO.class));

        verify(measurementsService, times(1))
                .save(isA(Measurement.class));
    }

    @Test
    void addMeasurement_whenFailedValidation_shouldReturnStatus400() throws Exception {
        MeasurementDTO measurementDTO = new MeasurementDTO(
                24.7f,
                false,
                new SensorDTO("Test sensor")
        );

        doAnswer((Answer<Void>) invocation -> {
            Object[] arguments = invocation.getArguments();

            BindingResult bindingResult = (BindingResult) arguments[1];
            bindingResult.rejectValue("sensor","",
                    "Sensor with this name does not exists");

            return null;
        }).when(measurementDtoValidator).validate(isA(MeasurementDTO.class), isA(BindingResult.class));

        mockMvc.perform(post("/measurements/add")
                        .contentType("application/json")
                        .content(JsonMapper.mapToJson(measurementDTO)))
                .andExpect(status().isBadRequest());

        verify(measurementDtoValidator, times(1))
                .validate(isA(MeasurementDTO.class), isA(BindingResult.class));

        verify(measurementMapper, times(0))
                .mapToMeasurement(any());

        verify(measurementsService, times(0))
                .save(any());
    }
}