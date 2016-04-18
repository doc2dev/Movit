package com.andela.movit.config;

public enum Constants {
    PLACE_NAME("PLACE"),
    ACTIVITY_NAME("ACTIVITY"),
    LATITUDE("LAT"),
    LONGITUDE("LNG");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
