package ru.bestk1ng.java.hw3.db.dao;

import ru.bestk1ng.java.hw3.db.DBConnectionFactory;
import ru.bestk1ng.java.hw3.models.Booking;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class BookingDao {
    public Booking getBooking(String reference) throws Exception {
        try (Connection connection = DBConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM bookings WHERE booking_ref=\"%s\"", reference);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Booking");
    }

    public boolean insertBooking(Booking booking) {
        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO bookings VALUES (?, ?, ?)")) {
            ps.setString(1, booking.getReference());
            ps.setDate(2, booking.getDate());
            ps.setDouble(3, booking.getTotalAmount());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Set<Booking> getBookings() throws Exception {
        try (Connection connection = DBConnectionFactory.getConnection();
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
        Date date = resultSet.getDate("book_date");
        Double totalAmount = resultSet.getDouble("total_amount");

        return new Booking(reference, date, totalAmount);
    }
}
