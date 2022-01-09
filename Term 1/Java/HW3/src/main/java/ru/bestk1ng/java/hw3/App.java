package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.db.DBDaoFacade;
import ru.bestk1ng.java.hw3.db.DBDownloader;
import ru.bestk1ng.java.hw3.db.DBWorker;

public final class App {
    private App() {}

    public static void main(String[] args) {
        // Download DB if needed
        //downloadDB();
        executeTasks();
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
        ChartBuilder chartBuilder = new ChartBuilder();

        // Task 1
        DBWorker.Report report1 = worker.getReport1();
        tableBuilder.buildTable("report_1", report1);
        System.out.println("Report for Task #1 saved");

        // Task 2
        DBWorker.Report report2 = worker.getReport2();
        tableBuilder.buildTable("report_2", report2);
        System.out.println("Report for Task #2 saved");

        // Task 3
        DBWorker.Report report3 = worker.getReport3();
        tableBuilder.buildTable("report_3", report3);
        System.out.println("Report for Task #3 saved");

        // Task 4
        DBWorker.Report report4 = worker.getReport4();
        tableBuilder.buildTable("report_4", report4);
        System.out.println("Report for Task #4 saved");

        // Task 5
        DBWorker.Chart chart5_1 = worker.getChart5("Москва", false);
        chartBuilder.buildChard("chart5_1", "Дни недели", "Количество рейсов", chart5_1);
        DBWorker.Chart chart5_2 = worker.getChart5("Москва", true);
        chartBuilder.buildChard("chart5_2", "Дни недели", "Количество рейсов", chart5_2);
        System.out.println("Charts for Task #5 saved");
    }
}
