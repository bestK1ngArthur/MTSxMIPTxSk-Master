package ru.bestk1ng.java.hw3.models;

public class BoardingPass {
    private String ticketNumber;
    private Integer flightId;
    private Integer boardingNumber;
    private String seatNumber;

    public BoardingPass(String ticketNumber, Integer flightId, Integer boardingNumber, String seatNumber) {
        this.ticketNumber = ticketNumber;
        this.flightId = flightId;
        this.boardingNumber = boardingNumber;
        this.seatNumber = seatNumber;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public Integer getFlightId() {
        return flightId;
    }

    public Integer getBoardingNumber() {
        return boardingNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }
}
