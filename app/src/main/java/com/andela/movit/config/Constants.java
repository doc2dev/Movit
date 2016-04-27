package com.andela.movit.config;

public enum Constants {
    PLACE_NAME("PLACE"),
    ACTIVITY_NAME("com.andela.intent.action.ACTIVITY"),
    LATITUDE("LAT"),
    LONGITUDE("LNG"),
    LOCATION("com.andela.intent.action.LOCATION"),
    TIMESTAMP("com.andela.intent.action.STAMP"),
    STATEMENT("com.andela.intent.action.STMT"),
    SERVICE_NAME("ActivityRecognitionService"),
    COMMAND("COMMAND"),
    CURRENT_VISIT("VISIT"),
    TIME_BEFORE_LOGGING("T_LOG");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
