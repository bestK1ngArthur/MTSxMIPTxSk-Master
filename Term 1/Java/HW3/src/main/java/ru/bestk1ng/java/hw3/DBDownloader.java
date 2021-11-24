package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.dao.*;
import ru.bestk1ng.java.hw3.models.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DBDownloader {
    private String baseUrl = "https://storage.yandexcloud.net/airtrans-small/";

    private JSONParser parser = new JSONParser();
    private DBDaoFacade daoFacade;

    public DBDownloader(DBDaoFacade daoFacade) {
        this.daoFacade = daoFacade;
    }

    private Function<String, Aircraft> mapAircraft = (line) -> {
        String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        String code = values[0];
        JSONObject model = null;
        try {
            String json = values[1]
                    .substring(1,values[1].length()-1)
                    .replace("\"\"", "\"");
            model = (JSONObject) parser.parse(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer range = Integer.valueOf(values[2]);

        return new Aircraft(code, model, range);
    };

    public void download() throws Exception {
        downloadAircrafts();
    }

    private void downloadAircrafts() throws Exception {
        List<Aircraft> aircrafts = getItems("aircrafts", mapAircraft);

        for(Aircraft aircraft:aircrafts) {
            daoFacade.aircraft.insertAircraft(aircraft);
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
}
