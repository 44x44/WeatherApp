package ru.eliseev.WeatherApp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MeasurementDTO {

    @NotNull(message = "Measurement should contain value")
    @Min(-100)
    @Max(100)
    private Float value;

    @NotNull(message = "Measurement should contain bool variable 'raining'")
    private Boolean raining;

    @NotNull(message = "Measurement should contain info about sensor")
    private SensorDTO sensor;

    public MeasurementDTO() {
    }

    public MeasurementDTO(Float value, Boolean raining, SensorDTO sensor) {
        this.value = value;
        this.raining = raining;
        this.sensor = sensor;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Boolean isRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
