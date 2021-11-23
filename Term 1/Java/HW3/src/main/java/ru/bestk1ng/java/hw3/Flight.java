package ru.bestk1ng.java.hw3;

import java.sql.Timestamp;

public class Flight {
    public enum Status {
        SCHEDULED, ONTIME, DELAYED, DEPARTED, ARRIVED, CANCELLED
    }

    private Integer flightId;
    private String flightNumber;
    private Timestamp scheduledDeparture;
    private Timestamp scheduledArrival;
    private String departureAirport;
    private String arrivalAirport;
    private Status status;
    private String aircraftCode;
    private Timestamp actualDeparture;
    private Timestamp actualArrival;
}
