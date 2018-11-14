package pl.szymonhanzel.alarmeclient.model;


import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Locale;

public class Alarm {

    private double longitude, latitude;
    private String macAddress;
    private Timestamp timestamp;

    public Alarm() {
    }

    public Alarm (double longitude, double latitude){
        macAddress="LBALBLALBAA";
        this.latitude=latitude;
        this.longitude=longitude;
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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
