package com.andela.movit.config;

public enum Constants {
    PLACE_NAME("PLACE"),
    ACTIVITY_NAME("ACTIVITY"),
    LATITUDE("LAT"),
    LONGITUDE("LNG"),
    TIMESTAMP("STAMP"),
    ELAPSED("ELAPSED"),
    SERVICE_NAME("ActivityRecognitionService"),
    ACTIVITY_ACTION("com.andela.intent.action.BROADCAST_ACTION");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
