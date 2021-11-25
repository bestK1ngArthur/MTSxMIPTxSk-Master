package ru.bestk1ng.java.hw3.db.dao;

import ru.bestk1ng.java.hw3.db.DBConnectionFactory;
import ru.bestk1ng.java.hw3.models.BoardingPass;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class BoardingPassDao {
    public BoardingPass getBoardingPass(String ticketNumber, Integer flightId) throws Exception {
        try (Connection connection = DBConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM boarding_passes WHERE ticket_no=\"%s\" AND flight_id=%d", ticketNumber, flightId);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Boarding");
    }

    public boolean insertBoardingPass(BoardingPass boardingPass) {
        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO boarding_passes VALUES (?, ?, ?, ?)")) {
            ps.setString(1, boardingPass.getTicketNumber());
            ps.setInt(2, boardingPass.getFlightId());
            ps.setInt(3, boardingPass.getBoardingNumber());
            ps.setString(4, boardingPass.getSeatNumber());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Set<BoardingPass> getBoardingPasses() throws Exception {
        try (Connection connection = DBConnectionFactory.getConnection();
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
