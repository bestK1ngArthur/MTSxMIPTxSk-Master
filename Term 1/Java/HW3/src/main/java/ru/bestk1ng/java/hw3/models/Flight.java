package ru.bestk1ng.java.hw3.models;

import java.sql.Date;

public class Flight {
    public enum Status {
        SCHEDULED, ONTIME, DELAYED, DEPARTED, ARRIVED, CANCELLED
    }

    private Integer flightId;
    private String flightNumber;
    private Date scheduledDeparture;
    private Date scheduledArrival;
    private String departureAirport;
    private String arrivalAirport;
    private Status status;
    private String aircraftCode;
    private Date actualDeparture;
    private Date actualArrival;

    public Flight(
            Integer flightId,
            String flightNumber,
            Date scheduledDeparture,
            Date scheduledArrival,
            String departureAirport,
            String arrivalAirport,
            Status status,
            String aircraftCode,
            Date actualDeparture,
            Date actualArrival
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

    public Date getScheduledDeparture() {
        return scheduledDeparture;
    }

    public Date getScheduledArrival() {
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

    public Date getActualDeparture() {
        return actualDeparture;
    }

    public Date getActualArrival() {
        return actualArrival;
    }
}
