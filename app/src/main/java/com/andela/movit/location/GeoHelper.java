/**
 * This class provides an operation for obtaining the name of a place, given the latitude and
 * longitude coordinates of the place. Since the operation is asynchronous, an operation is
 * provided for setting a callback to be invoked when the place name is determined.
 * */

package com.andela.movit.location;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class GeoHelper {
    private Context context;

    private IncomingStringCallback callback;

    private final String URL_BASE
            = "http://maps.googleapis.com/maps/api/geocode/json?sensor=true&latlng=";

    public GeoHelper(Context context) {
        this.context = context;
    }

    /**
     * Sets a callback to be invoked once the activity name has been detected.
     * @param callback the callback object to be invoked.
     * */

    public void setCallback(IncomingStringCallback callback) {
        this.callback = callback;
    }

    /**
     * Gets the name of a place using the latitude and longitude of the place.
     * @param latitude the latitude of the place
     * @param longitude the longitude of the place.
     * */

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
