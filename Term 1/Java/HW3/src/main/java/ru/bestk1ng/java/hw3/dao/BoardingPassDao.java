package ru.bestk1ng.java.hw3.dao;

import ru.bestk1ng.java.hw3.DbConnectionFactory;
import ru.bestk1ng.java.hw3.models.BoardingPass;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class BoardingPassDao {
    public BoardingPass getBoardingPass(String ticketNumber, Integer flightId) throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM boarding_passes WHERE ticket_no=\"%s\" AND flight_id=%d", ticketNumber, flightId);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Boarding");
    }

    public Set<BoardingPass> getBoardingPasses() throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM boarding_passes");
            Set<BoardingPass> boardingPasses = new HashSet<>();
            while (resultSet.next()) {
                BoardingPass boardingPass = extract(resultSet);
                boardingPasses.add(boardingPass);
            }
            return boardingPasses;
        }
    }

    private BoardingPass extract(ResultSet resultSet) throws Exception {
        String ticketNumber = resultSet.getString("ticket_no");
        Integer flightId = resultSet.getInt("flight_id");
        Integer boardingNumber = resultSet.getInt("boarding_no");
        String seatNumber = resultSet.getString("seat_no");

        return new BoardingPass(ticketNumber, flightId, boardingNumber, seatNumber);
    }
}
