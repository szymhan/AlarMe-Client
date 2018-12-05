package pl.szymonhanzel.alarmeclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;


public class GPSUpdatesLocationService extends IntentService {


    public static final String ACTION_PROCESS_UPDATES =
            "pl.szymonhanzel.alarmeclient.service.GPSUpdatesLocationService.action" +
                    ".PROCESS_UPDATES";

    private static final String TAG = GPSUpdatesLocationService.class.getSimpleName();


    public GPSUpdatesLocationService() {
        // Name the worker thread.
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    //MyApplication.setLastKnownLocation();
                    Log.i(TAG, "Location update ended successfully");
                }
            }
        }
    }
}