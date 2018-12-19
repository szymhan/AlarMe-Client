package pl.szymonhanzel.alarmeclient.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;


public class GPSUpdatesLocationService extends IntentService {


    public static final String ACTION_PROCESS_UPDATES =
            "pl.szymonhanzel.alarmeclient.service.GPSUpdatesLocationService.action" +
                    ".PROCESS_UPDATES";

    private static final String TAG = GPSUpdatesLocationService.class.getSimpleName();
    private static boolean currentlyProcessingLocation;


    public GPSUpdatesLocationService() {
        // Name the worker thread.
        super(TAG);
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service onCreate() invoked.");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);
        if(!currentlyProcessingLocation) {
            currentlyProcessingLocation =true;
            startForeground(1995,NotificationService.buildNotification());
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    FirebaseDataAnalyzeService.setLastKnownLocation(locations.get(0));
                    FirebaseDataAnalyzeService.saveData(locations);
                    Log.i(TAG, "Location update ended successfully");
                }
            }
        }
    }
}