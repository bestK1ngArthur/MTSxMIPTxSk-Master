package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.db.DBDaoFacade;
import ru.bestk1ng.java.hw3.db.DBDownloader;

public final class App {
    private App() {}

    public static void main(String[] args) {
        System.out.println("Start");


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

    private void downloadDB() {
        DBDaoFacade daoFacade = new DBDaoFacade();
        DBDownloader downloader = new DBDownloader(daoFacade, true);
        downloader.download();
    }
}
