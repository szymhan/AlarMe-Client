package pl.szymonhanzel.alarmeclient.model;


import com.google.firebase.Timestamp;

import pl.szymonhanzel.alarmeclient.context.MyApplication;

public class Alarm {

    private double longitude, latitude,altitude;
    private String token;
    private Timestamp timestamp;

    public Alarm() {
    }

    public Alarm (double longitude, double latitude,double altitude){
        token =MyApplication.getToken();
        this.latitude=latitude;
        this.longitude=longitude;
        this.altitude=altitude;
        timestamp = Timestamp.now();
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
