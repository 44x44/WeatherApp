package ru.eliseev.WeatherApp.util.vallidation;

public class SensorNotCreatedException extends RuntimeException{

    public SensorNotCreatedException(String message) {
        super(message);
    }
}
