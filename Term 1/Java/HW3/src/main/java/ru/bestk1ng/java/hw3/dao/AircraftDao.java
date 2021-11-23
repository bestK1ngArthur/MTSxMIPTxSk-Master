package ru.bestk1ng.java.hw3.dao;

import ru.bestk1ng.java.hw3.DbConnectionFactory;
import ru.bestk1ng.java.hw3.models.Aircraft;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AircraftDao {
    private JSONParser parser = new JSONParser();

    public Aircraft getAircraft(String code) throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM aircrafts WHERE aircraft_code=" + code);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("failed to get user");
    }

    public Set<Aircraft> getAircrafts() throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
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
