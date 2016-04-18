package com.andela.movit.listeners;

public interface PlaceNameCallback {
    void onPlaceNameFound(String placeName);
    void onError(String message);
}
