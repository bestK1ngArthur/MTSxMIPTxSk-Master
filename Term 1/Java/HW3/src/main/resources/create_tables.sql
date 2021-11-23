drop table if exists aircrafts;
create table aircrafts
(
    aircraft_code CHAR(3) not null primary key,
    model         JSONB   not null,
    range         INTEGER not null
);

drop table if exists airports;
create table airports
(
    airport_code CHAR(3) not null primary key,
    airport_name JSONB   not null,
    city         JSONB   not null,
    coordinates  POINT   not null,
    timezone     TEXT    not null
);

drop table if exists boarding_passes;
create table boarding_passes
(
    ticket_no   CHAR(13)   not null,
    flight_id   INTEGER    not null,
    boarding_no INTEGER    not null,
    seat_no     VARCHAR(4) not null,
    primary key (ticket_no, flight_id),
    foreign key (ticket_no) references tickets (ticket_no),
    foreign key (flight_id) references flights (flight_id),
    foreign key (seat_no) references seats (seat_no)
);

drop table if exists bookings;
create table bookings
(
    booking_ref  CHAR(6)        not null primary key,
    book_date    TIMESTAMPZ     not null,
    total_amount NUMERIC(10, 2) not null
);

drop table if exists flights;
create table flights
(
    flight_id           SERIAL      not null primary key,
    flight_no           CHAR(6)     not null,
    scheduled_departure TIMESTAMPZ  not null,
    scheduled_arrival   TIMESTAMPZ  not null,
    departure_airport   CHAR(3)     not null,
    arrival_airport     CHAR(3)     not null,
    status              VARCHAR(20) not null,
    aircraft_code       CHAR(3)     not null,
    actual_departure    TIMESTAMPZ,
    actual_arrival      TIMESTAMPZ,
    foreign key (departure_airport) references airports (airport_code),
    foreign key (arrival_airport) references airports (airport_code),
    foreign key (aircraft_code) references aircrafts (aircraft_code)
);

drop table if exists seats;
create table seats
(
    aircraft_code   CHAR(3)     not null,
    seat_no         VARCHAR(4)  not null,
    fare_conditions VARCHAR(10) not null,
    primary key (aircraft_code, seat_no)
);

drop table if exists ticket_flights;
create table ticket_flights
(
    ticket_no       CHAR(13)    not null,
    flight_id       INTEGER     not null,
    fare_conditions VARCHAR(10) not null,
    primary key (ticket_no, flight_id),
    foreign key (ticket_no) references tickets (ticket_no),
    foreign key (flight_id) references flights (flight_id)
);

drop table if exists tickets;
create table tickets
(
    ticket_no      CHAR(13)    not null primary key,
    book_ref       CHAR(6)     not null,
    passenger_id   VARCHAR(20) not null,
    passenger_name TEXT        not null,
    contact_data   JSONB,
    foreign key (book_ref) references bookings (book_ref)
);
