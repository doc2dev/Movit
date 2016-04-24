package com.andela.movit.location;

import android.content.Context;

import com.andela.movit.listeners.IncomingStringCallback;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class GeoHelper {
    private Context context;

    private IncomingStringCallback callback;

    private final String URL_BASE
            = "http://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=";

    public void setCallback(IncomingStringCallback callback) {
        this.callback = callback;
    }

    public GeoHelper(Context context) {
        this.context = context;
    }

    public void getPlaceName(double latitude, double longitude) {
        String latLng = Double.toString(latitude) + "," + Double.toString(longitude);
        String url = URL_BASE + latLng;
        fetchFromRestApi(url);
    }

    private void fetchFromRestApi(String url) {
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .setCallback(getCallback());
    }

    private FutureCallback<JsonObject> getCallback() {
        return new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e == null) {
                    String formattedAddress = extractAddress(result);
                    callback.onStringArrive(formattedAddress);
                }
            }
        };
    }

    private String extractAddress(JsonObject jsonObject) {
        return jsonObject
                .getAsJsonArray("results")
                .get(0)
                .getAsJsonObject()
                .get("formatted_address")
                .getAsString();
    }
}
