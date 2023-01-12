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
import ru.eliseev.WeatherApp.dto.SensorDTO;
import ru.eliseev.WeatherApp.model.Sensor;
import ru.eliseev.WeatherApp.service.SensorsService;
import ru.eliseev.WeatherApp.util.vallidation.SensorDtoValidator;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SensorsController.class)
class SensorsControllerTest {

    @MockBean
    private SensorsService sensorsService;

    @MockBean
    private SensorDtoValidator sensorDtoValidator;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_whenValidatedAndSaved_shouldReturnStatus200() throws Exception {
        SensorDTO sensorDTO = new SensorDTO("Test sensor");

        doNothing()
                .when(sensorDtoValidator)
                .validate(isA(SensorDTO.class), isA(BindingResult.class));

        doNothing()
                .when(sensorsService)
                .save(isA(Sensor.class));

        mockMvc.perform(post("/sensors/registration")
                        .contentType("application/json")
                        .content(JsonMapper.mapToJson(sensorDTO)))
                .andExpect(status().isOk());

        verify(sensorDtoValidator, times(1))
                .validate(isA(SensorDTO.class), isA(BindingResult.class));

        verify(sensorsService, times(1))
                .save(isA(Sensor.class));
    }

    @Test
    void register_whenFailedValidation_shouldReturnStatus400() throws Exception {
        SensorDTO sensorDTO = new SensorDTO("Test sensor");

        doAnswer((Answer<Void>) invocation -> {
            Object[] arguments = invocation.getArguments();

            BindingResult bindingResult = (BindingResult) arguments[1];
            bindingResult.rejectValue("name","",
                    "Sensor with this name already exists");

            return null;
        }).when(sensorDtoValidator).validate(isA(SensorDTO.class), isA(BindingResult.class));

        mockMvc.perform(post("/sensors/registration")
                        .contentType("application/json")
                        .content(JsonMapper.mapToJson(sensorDTO)))
                .andExpect(status().isBadRequest());

        verify(sensorDtoValidator, times(1))
                .validate(isA(SensorDTO.class), isA(BindingResult.class));

        verify(sensorsService, times(0))
                .save(any());
    }
}