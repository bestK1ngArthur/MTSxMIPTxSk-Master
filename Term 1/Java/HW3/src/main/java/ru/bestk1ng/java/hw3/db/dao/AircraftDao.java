package ru.bestk1ng.java.hw3.db.dao;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ru.bestk1ng.java.hw3.db.DBConnectionFactory;
import ru.bestk1ng.java.hw3.models.Aircraft;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AircraftDao {
    private JSONParser parser = new JSONParser();

    public Aircraft getAircraft(String code) throws Exception {
        try (Connection connection = DBConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM aircrafts WHERE aircraft_code=\"%s\"", code);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Aircraft");
    }

    public boolean insertAircraft(Aircraft aircraft) {
        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO aircrafts VALUES (?, ?, ?)")) {
            ps.setString(1, aircraft.getCode());
            ps.setString(2, aircraft.getModel().toJSONString());
            ps.setInt(3, aircraft.getRange());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public Set<Aircraft> getAircrafts() throws Exception {
        try (Connection connection = DBConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM aircrafts");
            Set<Aircraft> aircrafts = new HashSet<>();
            while (resultSet.next()) {
                Aircraft aircraft = extract(resultSet);
                aircrafts.add(aircraft);
            }
            return aircrafts;
        }
    }

    private Aircraft extract(ResultSet resultSet) throws Exception {
        String code = resultSet.getString("aircraft_code");
        JSONObject model = (JSONObject) parser.parse(resultSet.getString("model"));
        Integer range = resultSet.getInt("range");

        return new Aircraft(code, model, range);
    }
}
