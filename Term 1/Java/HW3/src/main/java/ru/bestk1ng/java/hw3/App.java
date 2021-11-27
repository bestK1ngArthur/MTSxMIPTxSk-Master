package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.db.DBDaoFacade;
import ru.bestk1ng.java.hw3.db.DBDownloader;
import ru.bestk1ng.java.hw3.db.DBWorker;

public final class App {
    private App() {}

    public static void main(String[] args) {
        System.out.println("Start");

        workWithDB();

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

    private static void downloadDB() {
        DBDaoFacade daoFacade = new DBDaoFacade();
        DBDownloader downloader = new DBDownloader(daoFacade, true);
        downloader.download();
    }

    private static void workWithDB() {
        DBDaoFacade daoFacade = new DBDaoFacade();
        DBWorker worker = new DBWorker(daoFacade);
        DBWorker.Report report = worker.getReport1();
        System.out.println("");
    }
}
