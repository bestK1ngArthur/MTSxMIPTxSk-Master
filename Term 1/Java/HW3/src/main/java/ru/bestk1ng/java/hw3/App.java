package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.models.Airport;

import java.util.Set;

public final class App {
    private App() {}

    public static void main(String[] args) {
        System.out.println("Start");

        DBDaoFacade daoFacade = new DBDaoFacade();
        DBDownloader downloader = new DBDownloader(daoFacade, true);
        downloader.download();

//        try {
//            Set<Airport> airports = daoFacade.airport.getAirports();
//            for (Airport airport:airports) {
//                System.out.println(airport.getName().get("en"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        System.out.println("End");
    }
}
