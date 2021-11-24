package ru.bestk1ng.java.hw3.dao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.bestk1ng.java.hw3.DbConnectionFactory;
import ru.bestk1ng.java.hw3.models.Ticket;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class TicketDao {
    private JSONParser parser = new JSONParser();

    public Ticket getTicket(String ticketNumber) throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM tickets WHERE ticket_no=\"%s\"", ticketNumber);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Seat");
    }

    public Set<Ticket> getTickets() throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
            Set<Ticket> tickets = new HashSet<>();
            while (resultSet.next()) {
                Ticket ticket = extract(resultSet);
                tickets.add(ticket);
            }
            return tickets;
        }
    }

    private Ticket extract(ResultSet resultSet) throws Exception {
        String ticketNumber = resultSet.getString("ticket_no");
        String bookingReference = resultSet.getString("book_ref");
        String passengerId = resultSet.getString("passenger_id");
        String passengerName = resultSet.getString("passenger_name");
        JSONObject contactData = (JSONObject) parser.parse(resultSet.getString("contact_data"));

        return new Ticket(ticketNumber, bookingReference, passengerId, passengerName, contactData);
    }
}
