package ru.bestk1ng.java.hw3.dao;

import ru.bestk1ng.java.hw3.DbConnectionFactory;
import ru.bestk1ng.java.hw3.models.Seat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class SeatDao {
    public Seat getSeat(String aircraftCode, String seatNumber) throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM seats WHERE aircraft_code=\"%s\" AND seat_no=\"%s\"", aircraftCode, seatNumber);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Seat");
    }

    public Set<Seat> getSeats() throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM seats");
            Set<Seat> seats = new HashSet<>();
            while (resultSet.next()) {
                Seat seat = extract(resultSet);
                seats.add(seat);
            }
            return seats;
        }
    }

    private Seat extract(ResultSet resultSet) throws Exception {
        String aircraftCode = resultSet.getString("aircraft_code");
        String seatNumber = resultSet.getString("seat_no");
        String fareConditions = resultSet.getString("fare_conditions");

        return new Seat(aircraftCode, seatNumber, fareConditions);
    }
}
