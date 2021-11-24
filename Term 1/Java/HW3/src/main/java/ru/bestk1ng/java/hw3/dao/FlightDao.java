package ru.bestk1ng.java.hw3.dao;

import ru.bestk1ng.java.hw3.DbConnectionFactory;
import ru.bestk1ng.java.hw3.models.Flight;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class FlightDao {
    public Flight getFlight(Integer flightId) throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM flights WHERE flight_id=\"%d\"", flightId);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Flight");
    }

    public Set<Flight> getFlights() throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
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
        Timestamp scheduledDeparture = Timestamp.valueOf(resultSet.getString("scheduled_departure"));
        Timestamp scheduledArrival = Timestamp.valueOf(resultSet.getString("scheduled_arrival"));
        String departureAirport = resultSet.getString("departure_airport");
        String arrivalAirport = resultSet.getString("arrival_airport");
        Flight.Status status = extractStatus(resultSet.getString("status"));
        String aircraftCode = resultSet.getString("aircraft_code");
        Timestamp actualDeparture = Timestamp.valueOf(resultSet.getString("actual_departure"));
        Timestamp actualArrival = Timestamp.valueOf(resultSet.getString("actual_arrival"));

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

    private Flight.Status extractStatus(String string) {
        switch (string) {
            case "Scheduled": return Flight.Status.SCHEDULED;
            case "On Time": return Flight.Status.ONTIME;
            case "Delayed": return Flight.Status.DELAYED;
            case "Departed": return Flight.Status.DEPARTED;
            case "Arrived": return Flight.Status.ARRIVED;
            case "Canceled": return Flight.Status.CANCELLED;
            default: return null;
        }
    }
}
