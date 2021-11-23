package ru.bestk1ng.java.hw3.models;

import org.json.simple.JSONObject;

public class Aircraft {
    private String code;
    private JSONObject model;
    private Integer range;

    public Aircraft(String code, JSONObject model, Integer range) {
        this.code = code;
        this.model = model;
        this.range = range;
    }

    public String getCode() {
        return code;
    }

    public JSONObject getModel() {
        return model;
    }

    public Integer getRange() {
        return range;
    }
}
