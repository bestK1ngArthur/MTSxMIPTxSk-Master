package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.dao.AircraftDao;
import ru.bestk1ng.java.hw3.models.Aircraft;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        System.out.println("Start");
//        try (Connection connection = DbConnectionFactory.getConnection();
//             PreparedStatement ps = connection.prepareStatement("INSERT INTO aircrafts VALUES (?, ?, ?)")) {
//            ps.setString(1, "BOI");
//            ps.setString(2, "{}");
//            ps.setInt(3,10);
//            boolean b = ps.executeUpdate() == 1;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
        try {
            AircraftDao dao = new AircraftDao();
            Aircraft aircraft = dao.getAircraft("BUS");
            System.out.println(aircraft);

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        System.out.println("End");
    }
}
