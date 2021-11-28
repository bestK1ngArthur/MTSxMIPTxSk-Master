package ru.bestk1ng.java.hw3.db;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.bestk1ng.java.hw3.models.Airport;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class DBWorker {
    private JSONParser jsonParser = new JSONParser();
    private DBDaoFacade daoFacade;

    public DBWorker(DBDaoFacade daoFacade) {
        this.daoFacade = daoFacade;
    }

    public Report getReport1() {
        List<String[]> report = new ArrayList<>();

        try {
            Dictionary<String, List<String>> dict = new Hashtable();

            for (Airport airport:daoFacade.airport.getAirports()) {
                String city = airport.getCity().get("ru").toString();
                List<String> airports = dict.get(city);
                if (airports == null) {
                    airports = new ArrayList<>();
                }
                airports.add(airport.getCode());
                dict.put(city, airports);
            }

            Enumeration<String> keys = dict.keys();
            while(keys.hasMoreElements()) {
                String city = keys.nextElement();
                List<String> airports = dict.get(city);

                if (airports.size() <= 1) { continue; }

                String airportsString = String.join(", ", airports.toArray(new String[airports.size()]));
                report.add(new String[]{ city, airportsString });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Report(
                new String[]{ "Город", "Список аэропортов" },
                report.toArray(new String[report.size()][])
        );
    }

    public Report getReport2() {
        List<String[]> report = new ArrayList<>();

        try (Connection connection = DBConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            Dictionary<String, Integer> dict = new Hashtable();

            String sql = """
            SELECT a.airport_code, a.city, f.departure_airport, f.status
            FROM airports a, flights f
            WHERE a.airport_code=f.departure_airport AND f.status='CANCELLED'
            """;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                JSONObject cityJson = (JSONObject) jsonParser.parse(resultSet.getString("city"));
                String city = (String) cityJson.get("ru");

                Integer count = dict.get(city);
                if (count == null) {
                    count = 0;
                }

                count++;
                dict.put(city, count);
            }

            Enumeration<String> keys = dict.keys();
            while(keys.hasMoreElements()) {
                String city = keys.nextElement();
                String count = dict.get(city).toString();
                report.add(new String[]{ city, count });
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return new Report(
                new String[]{ "Город", "Количество отменённых рейсов" },
                report.toArray(new String[report.size()][])
        );
    }

    public Report getReport4() {
        List<String[]> report = new ArrayList<>();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM", Locale.forLanguageTag("ru"));

        try (Connection connection = DBConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            Dictionary<String, Integer> dict = new Hashtable();

            String sql = """
            SELECT scheduled_departure, status FROM flights
            WHERE status='CANCELLED'
            """;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Date scheduledDeparture = resultSet.getDate("scheduled_departure");
                String month = dateFormatter.format(scheduledDeparture);
                String capitalizedMonth = Character.toUpperCase(month.charAt(0)) + month.substring(1);

                Integer count = dict.get(capitalizedMonth);
                if (count == null) {
                    count = 0;
                }

                count++;
                dict.put(capitalizedMonth, count);
            }

            Enumeration<String> keys = dict.keys();
            while(keys.hasMoreElements()) {
                String city = keys.nextElement();
                String count = dict.get(city).toString();
                report.add(new String[]{ city, count });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Report(
                new String[]{ "Месяц", "Количество отменённых рейсов" },
                report.toArray(new String[report.size()][])
        );
    }

    public class Report {
        public String[] rowNames;
        public String[][] rows;

        Report(String[] rowNames, String[][] rows) {
            this.rowNames = rowNames;
            this.rows = rows;
        }
    }
}
