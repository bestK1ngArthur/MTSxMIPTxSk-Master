package ru.bestk1ng.java.hw3.models;

public class TicketFlight {
    private String ticketNumber;
    private Integer flightId;
    private String fareConditions;

    public TicketFlight(String ticketNumber, Integer flightId, String fareConditions) {
        this.ticketNumber = ticketNumber;
        this.flightId = flightId;
        this.fareConditions = fareConditions;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public String getFareConditions() {
        return fareConditions;
    }
}
