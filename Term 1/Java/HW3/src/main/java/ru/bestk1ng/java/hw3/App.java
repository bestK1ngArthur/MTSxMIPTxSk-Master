package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.db.DBDaoFacade;
import ru.bestk1ng.java.hw3.db.DBDownloader;
import ru.bestk1ng.java.hw3.db.DBWorker;

public final class App {
    private App() {}

    public static void main(String[] args) {
        System.out.println("Start");
        executeTasks();
        System.out.println("Finish");
    }

    private static void downloadDB() {
        DBDaoFacade daoFacade = new DBDaoFacade();
        DBDownloader downloader = new DBDownloader(daoFacade, true);
        downloader.download();
    }

    private static void executeTasks() {
        DBDaoFacade daoFacade = new DBDaoFacade();
        DBWorker worker = new DBWorker(daoFacade);
        TableBuilder tableBuilder = new TableBuilder();

        // Task 1
        DBWorker.Report report1 = worker.getReport1();
        tableBuilder.buildTable("report_1", report1);
    }
}
