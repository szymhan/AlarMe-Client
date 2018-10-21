package pl.szymonhanzel.alarmeclient.service;

import android.location.Location;
import com.google.firebase.database.DataSnapshot;

public class FirebaseDataAnalyzeService {

    private static Location lastKnownLocation;

    public static void analyzeData (DataSnapshot ds) {

    }

    public static Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public static void setLastKnownLocation(Location lastKnownLocation) {
        FirebaseDataAnalyzeService.lastKnownLocation = lastKnownLocation;
    }
}
