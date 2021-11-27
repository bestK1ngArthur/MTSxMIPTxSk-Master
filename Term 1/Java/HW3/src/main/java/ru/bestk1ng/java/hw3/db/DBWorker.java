package ru.bestk1ng.java.hw3.db;

import ru.bestk1ng.java.hw3.models.Airport;

import java.util.*;
import java.util.stream.Collectors;

public class DBWorker {
    private DBDaoFacade daoFacade;

    public DBWorker(DBDaoFacade daoFacade) {
        this.daoFacade = daoFacade;
    }

    public Report1[] getReport1() {
        List<Report1> report = new ArrayList<>();

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
                report.add(new Report1(city, airports.toArray(new String[airports.size()])));
            }

            report = report.stream()
                    .filter( a -> a.airportCodes.length > 1 )
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return report.toArray(new Report1[report.size()]);
    }

    public class Report1 {
        String cityName;
        String[] airportCodes;

        Report1(String cityName, String[] airportCodes) {
            this.cityName = cityName;
            this.airportCodes = airportCodes;
        }
    }
}
