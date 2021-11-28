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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    public Report getReport3() {
        List<String[]> report = new ArrayList<>();

        try (Connection connection = DBConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            Dictionary<String, ArrayList<Long>> dict = new Hashtable();

            String sql = """
            SELECT a.airport_code, a.city, f.departure_airport, f.arrival_airport, f.actual_departure, f.actual_arrival
            FROM airports a, flights f
            WHERE a.airport_code=f.departure_airport AND f.actual_departure is not NULL AND f.actual_arrival is not NULL
            """;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                JSONObject cityJson = (JSONObject) jsonParser.parse(resultSet.getString("city"));
                String city = (String) cityJson.get("ru");
                String arrivalAirport = resultSet.getString("arrival_airport");
                String key = String.format("%s,%s", city, arrivalAirport);

                Date actualDeparture = resultSet.getDate("actual_departure");
                Date actualArrival = resultSet.getDate("actual_arrival");

                Long diffInMillies = Math.abs(actualArrival.getTime() - actualDeparture.getTime());
                Long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);

                ArrayList<Long> diffs = dict.get(key);
                if (diffs == null) {
                    diffs = new ArrayList<>();
                }

                diffs.add(diffInMinutes);
                dict.put(key,diffs);
            }

            Enumeration<String> keys = dict.keys();
            while(keys.hasMoreElements()) {
                String key = keys.nextElement();
                String city = key.split(",")[0];
                String arrivalAirport = key.split(",")[1];

                ArrayList<Long> diffs = dict.get(key);
                Long average = 0L;
                for (Long diff:diffs) {
                    average += diff;
                }
                average = average / diffs.size();

                report.add(new String[]{ city, arrivalAirport, String.format("%d", average) });
            }

            report = report
                    .stream().sorted( (l,r) -> { return Long.compare(Long.valueOf(l[2]), Long.valueOf(r[2])); })
                    .collect(Collectors.toList());

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return new Report(
                new String[]{ "Месяц", "Пункт прибытия", "Средняя продолжительность полёта (мин)" },
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

    public Chart getChart5(String city, Boolean isReturn) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE", Locale.forLanguageTag("ru"));

        String[] barNames = new String[] {"понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье"};
        Integer[] values = new Integer[7];

        try (Connection connection = DBConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            Dictionary<String, Integer> dict = new Hashtable();

            String sql = String.format("""
            SELECT a.airport_code, a.city, f.departure_airport, f.arrival_airport, f.scheduled_departure
            FROM airports a, flights f
            WHERE a.airport_code=f.%s
            """, isReturn ? "arrival_airport" : "departure_airport");
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                JSONObject cityJson = (JSONObject) jsonParser.parse(resultSet.getString("city"));
                String resultCity = (String) cityJson.get("ru");

                if (!city.equalsIgnoreCase(resultCity)) { continue; }

                Date scheduledDeparture = resultSet.getDate("scheduled_departure");
                String weekday = dateFormatter.format(scheduledDeparture);

                Integer count = dict.get(weekday);
                if (count == null) {
                    count = 0;
                }
                count++;
                dict.put(weekday, count);
            }

            for (int i = 0; i < barNames.length; i++) {
                values[i] = getValue(barNames[i], dict);
            }

        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

        return new Chart(barNames, values);
    }

    private Integer getValue(String key, Dictionary<String, Integer> dict) {
        Integer value = dict.get(key);
        if (value == null) {
            value = 0;
        }
        return value;
    }

    public class Report {
        public String[] rowNames;
        public String[][] rows;

        Report(String[] rowNames, String[][] rows) {
            this.rowNames = rowNames;
            this.rows = rows;
        }
    }

    public class Chart {
        public String[] barNames;
        public Integer[] values;

        Chart(String[] barNames, Integer[] values) {
            this.barNames = barNames;
            this.values = values;
        }
    }
}
