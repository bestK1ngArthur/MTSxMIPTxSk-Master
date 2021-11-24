package ru.bestk1ng.java.hw3.models;

public class Seat {
    private String aircraftCode;
    private String seatNumber;
    private String fareConditions;

    public Seat(String aircraftCode, String seatNumber, String fareConditions) {
        this.aircraftCode = aircraftCode;
        this.seatNumber = seatNumber;
        this.fareConditions = fareConditions;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getFareConditions() {
        return fareConditions;
    }
}
