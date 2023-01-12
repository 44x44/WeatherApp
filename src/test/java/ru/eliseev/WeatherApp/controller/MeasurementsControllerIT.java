package ru.eliseev.WeatherApp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.eliseev.WeatherApp.dto.MeasurementDTO;
import ru.eliseev.WeatherApp.dto.SensorDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
class MeasurementsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    @Sql(value = {"/sql/measurementsControllerTest/add_correct-before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/measurementsControllerTest/add_correct-after.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addMeasurement_correctMeasurement_shouldReturnStatus200() throws Exception {
        List<SensorDTO> sensors = new ArrayList<>();
        sensors.add(new SensorDTO("Test sensor1"));
        sensors.add(new SensorDTO("Test sensor2"));
        sensors.add(new SensorDTO("Test sensor3"));

        List<MeasurementDTO> measurements = create1000RandomMeasurements(sensors);

        for (MeasurementDTO measurementDTO : measurements) {
            String measurementDTOJson = JsonMapper.mapToJson(measurementDTO);

            this.mockMvc.perform(post("/measurements/add")
                            .contentType("application/json")
                            .content(measurementDTOJson))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @Sql(value = {"/sql/measurementsControllerTest/get_1000Measurements-before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/measurementsControllerTest/get_1000Measurements-after.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getMeasurements_when1000Measurements_shouldReturn1000Measurements() throws Exception {
        this.mockMvc.perform(get("/measurements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1000)));
    }

    @Test
    @Sql(value = {"/sql/measurementsControllerTest/get-rainy-days-count_when1000Measurements-before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/measurementsControllerTest/get-rainy-days-count_when1000Measurements-after.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getRainyDaysCount_when1000Measurements_shouldBeBetween0And1000() throws Exception {
        this.mockMvc.perform(get("/measurements/rainyDaysCount"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.rainyDaysCount",greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.rainyDaysCount",lessThanOrEqualTo(1000)));
    }

    @Test
    @Sql(value = {"/sql/measurementsControllerTest/fill-sample-data-before.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/sql/measurementsControllerTest/fill-sample-data-after.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void fillSampleData() throws Exception {
        List<SensorDTO> sensors = new ArrayList<>();
        sensors.add(new SensorDTO("Test sensor1"));
        sensors.add(new SensorDTO("Test sensor2"));
        sensors.add(new SensorDTO("Test sensor3"));

        List<MeasurementDTO> measurements = create1000RandomMeasurements(sensors);

        for (MeasurementDTO measurementDTO : measurements) {
            String measurementDTOJson = JsonMapper.mapToJson(measurementDTO);

            this.mockMvc.perform(post("/measurements/add")
                            .contentType("application/json")
                            .content(measurementDTOJson))
                    .andExpect(status().isOk());
        }
    }

    private List<MeasurementDTO> create1000RandomMeasurements(List<SensorDTO> sensors) {
        Random random = new Random();
        List<MeasurementDTO> measurements = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            double rawValue = random.nextDouble() * 200 - 100;
            double scale = Math.pow(10, 1);
            Float value = (float) (Math.round(rawValue * scale) / scale);

            MeasurementDTO measurementDTO = new MeasurementDTO(
                    value,
                    random.nextBoolean(),
                    sensors.get(i % sensors.size())
            );

            measurements.add(measurementDTO);
        }

        return measurements;
    }
}