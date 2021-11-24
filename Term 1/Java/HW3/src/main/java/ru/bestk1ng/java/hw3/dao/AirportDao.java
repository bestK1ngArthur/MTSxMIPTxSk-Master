package ru.bestk1ng.java.hw3.dao;

import ru.bestk1ng.java.hw3.DbConnectionFactory;
import ru.bestk1ng.java.hw3.models.Airport;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class AirportDao {
    private JSONParser parser = new JSONParser();

    public Airport getAirport(String code) throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = String.format("SELECT * FROM airports WHERE airport_code=\"%s\"", code);
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return extract(resultSet);
            }
        }
        throw new RuntimeException("Failed to get Airport");
    }

    public Set<Airport> getAirports() throws Exception {
        try (Connection connection = DbConnectionFactory.getConnection();
             Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM airports");
            Set<Airport> airports = new HashSet<>();
            while (resultSet.next()) {
                Airport airport = extract(resultSet);
                airports.add(airport);
            }
            return airports;
        }
    }

    private Airport extract(ResultSet resultSet) throws Exception {
        String code = resultSet.getString("airport_code");
        JSONObject name = (JSONObject) parser.parse(resultSet.getString("airport_name"));
        JSONObject city = (JSONObject) parser.parse(resultSet.getString("city"));
        Point coordinates = extractPoint(resultSet.getString("coordinates"));
        TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of(resultSet.getString("timezone")));

        return new Airport(code, name, city, coordinates, timeZone);
    }

    private Point extractPoint(String string) {
        String[] values = string
                .replace("(", "")
                .replace(")", "")
                .split(",");
        Point point = new Point();
        point.x = Integer.parseInt(values[0]);
        point.y = Integer.parseInt(values[1]);
        return point;
    }
}
