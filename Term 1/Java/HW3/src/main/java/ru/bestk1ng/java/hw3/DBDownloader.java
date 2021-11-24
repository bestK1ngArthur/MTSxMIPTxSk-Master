package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.dao.*;
import ru.bestk1ng.java.hw3.models.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DBDownloader {
    private String baseUrl = "https://storage.yandexcloud.net/airtrans-small/";
    private String commaRegex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    private JSONParser parser = new JSONParser();
    private DBDaoFacade daoFacade;

    public DBDownloader(DBDaoFacade daoFacade) {
        this.daoFacade = daoFacade;
    }

    private Function<String, Aircraft> mapAircraft = (line) -> {
        String[] values = line.split(commaRegex);

        String code = values[0];
        JSONObject model = parseJson(values[1]);
        Integer range = Integer.valueOf(values[2]);

        return new Aircraft(code, model, range);
    };

    private Function<String, Airport> mapAirport = (line) -> {
        String[] values = line.split(commaRegex);

        String code = values[0];
        JSONObject name = parseJson(values[1]);
        JSONObject city = parseJson(values[2]);
        Airport.Coordinates coordinates = new Airport.Coordinates(values[3].substring(1, values[3].length()-1));
        TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of(values[4]));

        return new Airport(code, name, city, coordinates, timeZone);
    };

    public void download() throws Exception {
        downloadAircrafts();
        downloadAirports();
    }

    private void downloadAircrafts() throws Exception {
        List<Aircraft> aircrafts = getItems("aircrafts", mapAircraft);

        for(Aircraft aircraft:aircrafts) {
            daoFacade.aircraft.insertAircraft(aircraft);
        }
    }

    private void downloadAirports() throws Exception {
        List<Airport> airports= getItems("airports", mapAirport);

        for(Airport airport:airports) {
            daoFacade.airport.insertAirport(airport);
        }
    }

    private <Item> List<Item> getItems(String fileName, Function<String, Item> map) throws Exception {
        URL url = new URL(baseUrl + fileName + ".csv");

        List<Item> objects = new ArrayList<Item>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            objects = br.lines()
                    .skip(1)
                    .map(map)
                    .collect(Collectors.toList());
        }
        return objects;
    }

    private JSONObject parseJson(String string) {
        String raw = string
                .substring(1,string.length()-1)
                .replace("\"\"", "\"");
        try {
            return (JSONObject) parser.parse(raw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
