package ru.bestk1ng.java.hw3.models;

import org.json.simple.JSONObject;

import java.awt.*;
import java.util.TimeZone;

public class Airport {
    private String code;
    private JSONObject name;
    private JSONObject city;
    private Point coordinates;
    private TimeZone timeZone;

    public Airport(String code, JSONObject name, JSONObject city, Point coordinates, TimeZone timeZone) {
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

    public Point getCoordinates() {
        return coordinates;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }
}
