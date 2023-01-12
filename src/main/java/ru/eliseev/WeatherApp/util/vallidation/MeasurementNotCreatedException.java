package ru.eliseev.WeatherApp.util.vallidation;

public class MeasurementNotCreatedException extends RuntimeException{

    public MeasurementNotCreatedException(String message) {
        super(message);
    }
}
