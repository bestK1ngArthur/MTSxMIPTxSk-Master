package ru.bestk1ng.java.hw3.models;

import org.json.simple.JSONObject;

import java.awt.*;
import java.util.TimeZone;

public class Airport {
    private String code;
    private JSONObject name;
    private JSONObject city;
    private Coordinates coordinates;
    private TimeZone timeZone;

    public Airport(
            String code,
            JSONObject name,
            JSONObject city,
            Coordinates coordinates,
            TimeZone timeZone
    ) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.coordinates = coordinates;
        this.timeZone = timeZone;
    }

    public String getCode() {
        return code;
    }

    public JSONObject getName() {
        return name;
    }

    public JSONObject getCity() {
        return city;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public static class Coordinates {
        public Float latitude;
        public Float longitude;

        public Coordinates(Float latitude, Float longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Coordinates(String string) {
            String[] values = string
                    .replace("(", "")
                    .replace(")", "")
                    .split(",");
            latitude = Float.parseFloat(values[0]);
            longitude = Float.parseFloat(values[1]);
        }

        public String toString() {
            return String.format("(%f,%f)", latitude, longitude);
        }
    }
}
