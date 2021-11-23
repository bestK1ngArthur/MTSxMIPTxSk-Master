package ru.bestk1ng.java.hw3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        System.out.println("Start");
        try (Connection connection = DbConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO aircrafts VALUES (?, ?, ?)")) {
            ps.setString(1, "BOI");
            ps.setString(2, "{}");
            ps.setInt(3,10);
            boolean b = ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("End");
    }
}
