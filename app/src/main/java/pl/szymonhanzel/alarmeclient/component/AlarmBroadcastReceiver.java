package pl.szymonhanzel.alarmeclient.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;

import pl.szymonhanzel.alarmeclient.service.FirebaseDataAnalyzeService;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.backgroundlocationupdates.action" +
                    ".PROCESS_UPDATES";

    private static final String TAG = AlarmBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    //MyContext.setLastKnownLocation();
                    FirebaseDataAnalyzeService.saveData(locations);
                    Log.i(TAG, "Location update ended successfully");
                }
            }
        }

    }
}
