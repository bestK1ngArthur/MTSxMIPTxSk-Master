package ru.bestk1ng.java.hw3.db;

import ru.bestk1ng.java.hw3.models.Airport;

import java.util.*;

public class DBWorker {
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

    public class Report {
        public String[] rowNames;
        public String[][] rows;

        Report(String[] rowNames, String[][] rows) {
            this.rowNames = rowNames;
            this.rows = rows;
        }
    }
}
