package ru.bestk1ng.java.hw3;

import ru.bestk1ng.java.hw3.dao.*;

public class DBDaoFacade {
    public AircraftDao aircraft = new AircraftDao();
    public AirportDao airport = new AirportDao();
    public BoardingPassDao boardingPass = new BoardingPassDao();
    public BookingDao booking = new BookingDao();
    public FlightDao flight = new FlightDao();
    public SeatDao seat = new SeatDao();
    public TicketDao ticket = new TicketDao();
    public TicketFlightDao ticketFlight = new TicketFlightDao();
}
