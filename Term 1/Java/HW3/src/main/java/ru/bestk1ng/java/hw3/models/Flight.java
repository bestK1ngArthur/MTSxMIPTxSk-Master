package ru.bestk1ng.java.hw3.models;

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

    public Flight(
            Integer flightId,
            String flightNumber,
            Timestamp scheduledDeparture,
            Timestamp scheduledArrival,
            String departureAirport,
            String arrivalAirport,
            Status status,
            String aircraftCode,
            Timestamp actualDeparture,
            Timestamp actualArrival
    ) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.status = status;
        this.aircraftCode = aircraftCode;
        this.actualDeparture = actualDeparture;
        this.actualArrival = actualArrival;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Timestamp getScheduledDeparture() {
        return scheduledDeparture;
    }

    public Timestamp getScheduledArrival() {
        return scheduledArrival;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public Status getStatus() {
        return status;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public Timestamp getActualDeparture() {
        return actualDeparture;
    }

    public Timestamp getActualArrival() {
        return actualArrival;
    }
}
