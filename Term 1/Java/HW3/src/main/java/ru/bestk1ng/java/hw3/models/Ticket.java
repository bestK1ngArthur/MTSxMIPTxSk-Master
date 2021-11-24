package ru.bestk1ng.java.hw3.models;

import org.json.simple.JSONObject;

public class Ticket {
    private String ticketNumber;
    private String bookingReference;
    private String passengerId;
    private String passengerName;
    private JSONObject contactData;

    public Ticket(
            String ticketNumber,
            String bookingReference,
            String passengerId,
            String passengerName,
            JSONObject contactData
    ) {
        this.ticketNumber = ticketNumber;
        this.bookingReference = bookingReference;
        this.passengerId = passengerId;
        this.passengerName = passengerName;
        this.contactData = contactData;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public JSONObject getContactData() {
        return contactData;
    }
}
