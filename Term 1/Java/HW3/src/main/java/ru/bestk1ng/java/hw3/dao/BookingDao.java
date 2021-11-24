package ru.bestk1ng.java.hw3.dao;

import ru.bestk1ng.java.hw3.DbConnectionFactory;
import ru.bestk1ng.java.hw3.models.Booking;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class BookingDao {
    public Booking getBooking(String reference) throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM bookings WHERE booking_ref=\"%s\"", reference);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Booking");
    }

    public Set<Booking> getBookings() throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM bookings");
            Set<Booking> bookings = new HashSet<>();
            while (resultSet.next()) {
                Booking booking = extract(resultSet);
                bookings.add(booking);
            }
            return bookings;
        }
    }

    private Booking extract(ResultSet resultSet) throws Exception {
        String reference = resultSet.getString("booking_ref");
        Date date = Date.valueOf(resultSet.getString("book_date"));
        Long totalAmount = resultSet.getLong("total_amount");

        return new Booking(reference, date, totalAmount);
    }
}
