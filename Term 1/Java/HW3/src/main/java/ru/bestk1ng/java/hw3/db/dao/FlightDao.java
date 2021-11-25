package ru.bestk1ng.java.hw3.db.dao;

import ru.bestk1ng.java.hw3.db.DBConnectionFactory;
import ru.bestk1ng.java.hw3.models.Flight;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class FlightDao {
    public Flight getFlight(Integer flightId) throws Exception {
        try (Connection connection = DBConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM flights WHERE flight_id=\"%d\"", flightId);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Flight");
    }

    public boolean insertFlight(Flight flight) {
        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO flights VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setInt(1, flight.getFlightId());
            ps.setString(2, flight.getFlightNumber());
            ps.setDate(3, flight.getScheduledDeparture());
            ps.setDate(4, flight.getScheduledArrival());
            ps.setString(5, flight.getDepartureAirport());
            ps.setString(6, flight.getArrivalAirport());
            ps.setString(7, flight.getStatus().toString());
            ps.setString(8, flight.getAircraftCode());
            ps.setDate(9, flight.getActualDeparture());
            ps.setDate(10, flight.getActualArrival());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Set<Flight> getFlights() throws Exception {
        try (Connection connection = DBConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM flights");
            Set<Flight> flights = new HashSet<>();
            while (resultSet.next()) {
                Flight flight = extract(resultSet);
                flights.add(flight);
            }
            return flights;
        }
    }

    private Flight extract(ResultSet resultSet) throws Exception {
        Integer flightId = resultSet.getInt("flight_id");
        String flightNumber = resultSet.getString("flight_no");
        Date scheduledDeparture = resultSet.getDate("scheduled_departure");
        Date scheduledArrival = resultSet.getDate("scheduled_arrival");
        String departureAirport = resultSet.getString("departure_airport");
        String arrivalAirport = resultSet.getString("arrival_airport");
        Flight.Status status = Flight.Status.valueOf(resultSet.getString("status"));
        String aircraftCode = resultSet.getString("aircraft_code");
        Date actualDeparture = resultSet.getDate("actual_departure");
        Date actualArrival = resultSet.getDate("actual_arrival");

        return new Flight(
                flightId,
                flightNumber,
                scheduledDeparture,
                scheduledArrival,
                departureAirport,
                arrivalAirport,
                status,
                aircraftCode,
                actualDeparture,
                actualArrival
        );
    }
}
