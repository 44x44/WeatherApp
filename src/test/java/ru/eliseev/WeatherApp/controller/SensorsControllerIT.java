package ru.eliseev.WeatherApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.eliseev.WeatherApp.dto.SensorDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class SensorsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    @Sql(value = {"/sql/sensorsControllerTest/register_correct-before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/sensorsControllerTest/register_correct-after.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void register_correctSensor_shouldReturnStatus200() throws Exception {
        for (int i = 0; i < 1000; i++) {
            SensorDTO sensorDTO = new SensorDTO("Test sensor" + i);
            String sensorDTOJson = JsonMapper.mapToJson(sensorDTO);

            this.mockMvc.perform(post("/sensors/registration")
                            .contentType("application/json")
                            .content(sensorDTOJson))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @Sql(value = {"/sql/sensorsControllerTest/register_incorrect-before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/sensorsControllerTest/register_incorrect-after.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void register_withIncorrectName_shouldReturnStatus400() throws Exception {
        SensorDTO sensorDTO = new SensorDTO("");
        String sensorDTOJson = JsonMapper.mapToJson(sensorDTO);

        this.mockMvc.perform(post("/sensors/registration")
                        .contentType("application/json")
                        .content(sensorDTOJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = {"/sql/sensorsControllerTest/register_existing-before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/sensorsControllerTest/register_existing-after.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void register_alreadyExistingSensor_shouldReturnStatus400() throws Exception {
        SensorDTO sensorDTO = new SensorDTO("Test sensor");
        String sensorDTOJson = JsonMapper.mapToJson(sensorDTO);

        this.mockMvc.perform(post("/sensors/registration")
                        .contentType("application/json")
                        .content(sensorDTOJson))
                .andExpect(status().isBadRequest());
    }
}