package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.models.Aircraft;

import java.util.Set;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        System.out.println("Start");

        DBDaoFacade daoFacade = new DBDaoFacade();
        DBDownloader downloader = new DBDownloader(daoFacade);
        try {
            downloader.download();
            Set<Aircraft> aircrafts = daoFacade.aircraft.getAircrafts();
            for (Aircraft aircraft:aircrafts) {
                System.out.println(aircraft.getModel().get("ru"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("End");
    }
}
