package ru.bestk1ng.java.hw3.models;

import java.sql.Date;

public class Booking {
    private String reference;
    private Date date;
    private Double totalAmount;

    public Booking(String reference, Date date, Double totalAmount) {
        this.reference = reference;
        this.date = date;
        this.totalAmount = totalAmount;
    }

    public String getReference() {
        return reference;
    }

    public Date getDate() {
        return date;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }
}
