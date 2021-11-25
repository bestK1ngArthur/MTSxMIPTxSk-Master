package ru.bestk1ng.java.hw3.dao;

import ru.bestk1ng.java.hw3.DbConnectionFactory;
import ru.bestk1ng.java.hw3.models.TicketFlight;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class TicketFlightDao {
    public TicketFlight getTicketFlight(String ticketNumber, Integer flightId) throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM ticket_flights WHERE ticket_no=\"%s\" AND flight_id=\"%d\"", ticketNumber, flightId);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Seat");
    }

    public boolean insertTicketFlight(TicketFlight ticketFlight) {
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO ticket_flights VALUES (?, ?, ?)")) {
            ps.setString(1, ticketFlight.getTicketNumber());
            ps.setInt(2, ticketFlight.getFlightId());
            ps.setString(3, ticketFlight.getFareConditions());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Set<TicketFlight> getTicketFlights() throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM ticket_flights");
            Set<TicketFlight> ticketFlights = new HashSet<>();
            while (resultSet.next()) {
                TicketFlight ticketFlight = extract(resultSet);
                ticketFlights.add(ticketFlight);
            }
            return ticketFlights;
        }
    }

    private TicketFlight extract(ResultSet resultSet) throws Exception {
        String ticketNumber = resultSet.getString("ticket_no");
        Integer flightId = resultSet.getInt("flight_id");
        String fareConditions = resultSet.getString("fare_conditions");

        return new TicketFlight(ticketNumber, flightId, fareConditions);
    }
}
