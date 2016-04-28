/**
 * This class defines a movement. It stores the activity that was being performed, the place
 * where the activity was performed (place name, latitude and longitude), the time at which the
 * movement was recorded and the duration over which the activity was done.
 * */

package com.andela.movit.models;

public class Movement {

    private String placeName;

    private String activityName;

    private double latitude;

    private double longitude;

    private long timeStamp;

    private long duration;

    public long getTimeStamp() {
        return timeStamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
