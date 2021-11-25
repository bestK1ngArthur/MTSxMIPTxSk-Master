package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.models.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DBDownloader {
    private String baseUrl = "https://storage.yandexcloud.net/airtrans-small/";
    private String commaRegex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    private JSONParser parser = new JSONParser();

    private DBDaoFacade daoFacade;
    private Boolean isLogEnabled;

    public DBDownloader(DBDaoFacade daoFacade, Boolean isLogEnabled) {
        this.daoFacade = daoFacade;
        this.isLogEnabled = isLogEnabled;
    }

    public void download() throws Exception {
//        downloadAircrafts();
//        downloadAirports();
//        downloadBoardingPasses();
//        downloadBookings();
        downloadFlights();
    }

    private void downloadAircrafts() throws Exception {
        Function<String, Aircraft> map = (line) -> {
            String[] values = parseLine(line);

            String code = values[0];
            JSONObject model = parseJson(values[1]);
            Integer range = Integer.valueOf(values[2]);

            return new Aircraft(code, model, range);
        };

        Function<Aircraft, Void> save = (aircraft) -> {
            daoFacade.aircraft.insertAircraft(aircraft);
            return null;
        };

        downloadItems("aircrafts", map, save);
    }

    private void downloadAirports() throws Exception {
        Function<String, Airport> map = (line) -> {
            String[] values = parseLine(line);

            String code = values[0];
            JSONObject name = parseJson(values[1]);
            JSONObject city = parseJson(values[2]);
            Airport.Coordinates coordinates = new Airport.Coordinates(values[3]);
            TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of(values[4]));

            return new Airport(code, name, city, coordinates, timeZone);
        };

        Function<Airport, Void> save = (airport) -> {
            daoFacade.airport.insertAirport(airport);
            return null;
        };

        downloadItems("airports", map, save);
    }

    private void downloadBoardingPasses() throws Exception {
        Function<String, BoardingPass> map = (line) -> {
            String[] values = parseLine(line);

            String ticketNumber = values[0];
            Integer flightId = Integer.valueOf(values[1]);
            Integer boardingNumber = Integer.valueOf(values[2]);
            String seatNumber = values[3];

            return new BoardingPass(ticketNumber, flightId, boardingNumber, seatNumber);
        };

        Function<BoardingPass, Void> save = (boardingPass) -> {
            daoFacade.boardingPass.insertBoardingPass(boardingPass);
            return null;
        };

        downloadItems("boarding_passes", map, save);
    }

    private void downloadBookings() throws Exception {
        Function<String, Booking> map = (line) -> {
            String[] values = parseLine(line);

            String reference = values[0];
            Date date = parseDate(values[1]);
            Double totalAmount = Double.valueOf(values[2]);

            return new Booking(reference, date, totalAmount);
        };

        Function<Booking, Void> save = (booking) -> {
            daoFacade.booking.insertBooking(booking);
            return null;
        };

        downloadItems("bookings", map, save);
    }

    private void downloadFlights() throws Exception {
        Function<String, Flight> map = (line) -> {
            String[] values = parseLine(line);

            Integer flightId = Integer.valueOf(values[0]);
            String flightNumber = values[1];
            Date scheduledDeparture = parseDate(values[2]);
            Date scheduledArrival = parseDate(values[3]);
            String departureAirport = values[4];
            String arrivalAirport = values[5];
            Flight.Status status = parseStatus(values[6]);
            String aircraftCode = values[7];

            Date actualDeparture = null;
            if (values.length >= 9) {
                actualDeparture = parseDate(values[8]);
            }

            Date actualArrival = null;
            if (values.length >= 10) {
                actualArrival = parseDate(values[9]);
            }

            return new Flight(
                    flightId,
                    flightNumber,
                    scheduledDeparture,
                    scheduledArrival,
                    departureAirport,
                    arrivalAirport,
                    status,
                    aircraftCode,
                    actualDeparture,
                    actualArrival
            );
        };

        Function<Flight, Void> save = (flight) -> {
            daoFacade.flight.insertFlight(flight);
            return null;
        };

        downloadItems("flights", map, save);
    }

    private <Item> void downloadItems(String fileName, Function<String, Item> map, Function<Item, Void> save) {
        Long progressStartTime = System.currentTimeMillis();
        printFileName(fileName);

        try {
            List<Item> items = getItems(fileName, map);
            saveItems(items, progressStartTime, save);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <Item> List<Item> getItems(String fileName, Function<String, Item> map) throws Exception {
        URL url = new URL(baseUrl + fileName + ".csv");

        List<Item> objects;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            objects = br.lines()
                    .skip(1)
                    .map(map)
                    .collect(Collectors.toList());
        }
        return objects;
    }

    private <Item> void saveItems(List<Item> items, Long progressStartTime, Function<Item, Void> save) {
        Long progressTotal = (long) items.size();

        for(int i = 0; i < items.size(); i++) {
            Long progressCurrent = Long.valueOf(i+1);
            save.apply(items.get(i));
            printProgress(progressStartTime, progressTotal, progressCurrent);
        }

        printEndProgress();
    }

    private JSONObject parseJson(String string) {
        String raw = string
                .replace("\"\"", "\"");
        try {
            return (JSONObject) parser.parse(raw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String[] parseLine(String line) {
        String[] rawValues = line.split(commaRegex);
        String[] values = new String[rawValues.length];
        for (int i = 0; i < rawValues.length; i++) {
            if ((rawValues[i].charAt(0) == '"') && (rawValues[i].charAt(rawValues[i].length() - 1) == '"')) {
                values[i] = rawValues[i].substring(1, rawValues[i].length() - 1);
            } else {
                values[i] = rawValues[i];
            }
        }
        return values;
    }

    private Date parseDate(String string) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");

        java.util.Date date = null;
        try {
            date = dateFormatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new java.sql.Date(date.getTime());
    }

    private Flight.Status parseStatus(String string) {
        switch (string) {
            case "Scheduled": return Flight.Status.SCHEDULED;
            case "On Time": return Flight.Status.ONTIME;
            case "Delayed": return Flight.Status.DELAYED;
            case "Departed": return Flight.Status.DEPARTED;
            case "Arrived": return Flight.Status.ARRIVED;
            case "Cancelled": return Flight.Status.CANCELLED;
            default: return null;
        }
    }

    private void printFileName(String fileName) {
        if (!isLogEnabled) { return; }

        System.out.println(String.format("Downloading file: %s.csv", fileName));
    }

    private void printProgress(long startTime, long total, long current) {
        if (!isLogEnabled) { return; }

        long eta = current == 0 ? 0 :
                (total - current) * (System.currentTimeMillis() - startTime) / current;

        String etaHms = current == 0 ? "N/A" :
                String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
                        TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

        StringBuilder string = new StringBuilder(80);
        int percent = (int) (current * 100 / total);
        string
                .append('\r')
                .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
                .append(String.format(" %d%% [", percent))
                .append(String.join("", Collections.nCopies(percent, "=")))
                .append('>')
                .append(String.join("", Collections.nCopies(100 - percent, " ")))
                .append(']')
                .append(String.join("", Collections.nCopies(current == 0 ? (int) (Math.log10(total)) : (int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
                .append(String.format(" %d/%d, ETA: %s", current, total, etaHms));

        System.out.print(string);
    }

    private void printEndProgress() {
        if (!isLogEnabled) { return; }

        System.out.println();
    }
}
