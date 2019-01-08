package pl.szymonhanzel.alarmeclient.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.context.MyContext;


public class GPSUpdatesLocationService extends IntentService {


    public static final String ACTION_PROCESS_UPDATES =
            "pl.szymonhanzel.alarmeclient.service.GPSUpdatesLocationService.action" +
                    ".PROCESS_UPDATES";

    private static final String TAG = GPSUpdatesLocationService.class.getSimpleName();
    private static final String NOTIFICATION_CHANNEL_ID = "1996";
    private static final String NOTIFICATION_CHANNEL_NAME = "alarme" ;
    private static final String NOTIFICATION_CHANNEL_DESC = "Praca w tle";
    private static final int NOTIFICATION_ID = 1996;
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
        Log.d(TAG,"OnStartCommand called");
            if(!currentlyProcessingLocation) {
                currentlyProcessingLocation = true;
                startInForeground();
                buildSingleNotification();

            }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "OnDestroy() called");
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

    /*private void startInForeground() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyContext.getContext(),NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle("AlarMe")
                .setContentText("Praca w tle")
                .setOngoing(true)
                .setAutoCancel(false);
        Notification notification=builder.build();
        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
           // channel.setSound(null,null);
            channel.setDescription(NOTIFICATION_CHANNEL_DESC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID, notification);
    }*/

    private void startInForeground() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyContext.getContext(),NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle("AlarMe")
                .setContentText("Praca w tle")
                .setOngoing(false)
                .setAutoCancel(false);
        Notification notification=builder.build();
        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NOTIFICATION_CHANNEL_DESC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID, notification);
    }

    public static void buildSingleNotification() {
        String textTitle = "AlarMe";
        String textContent = "Praca w tle";

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyContext.getContext());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyContext.getContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle(textTitle)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
// notificationId is a unique int for each notification that you must define
        int notificationID =(int) (Math.random()*100);
        notificationManager.notify(notificationID, mBuilder.build());
    }

}