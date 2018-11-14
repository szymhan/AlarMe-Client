package pl.szymonhanzel.alarmeclient.service;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;

import android.app.Service;
import android.content.Intent;
import android.location.Location;

import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.Nullable;

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.context.MyApplication;
import pl.szymonhanzel.alarmeclient.model.Alarm;


public class GPSService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{


    private static final String TAG = "GPSService";
    private static final String CHANNEL_ID = "1995";
    private static int notificationID;
    private static int PERMISSION_ACCESS_FINE_LOCATION = 1;

    private boolean currentlyProcessingLocation = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!currentlyProcessingLocation) {
            currentlyProcessingLocation =true;
            startTracking();
        }

        return START_NOT_STICKY;
    }

    private void startTracking() {
        Log.d(TAG, "startTracking");
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
            googleApiClient.connect();
            buildNotification();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@android.support.annotation.Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(120000); // milliseconds
        locationRequest.setFastestInterval(10000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException se) {
            Log.e(TAG, "Go into settings and find Gps Tracker app and enable Location.");
            //TODO: zrobic notyfikacje, ze cos nie dziala z listenerem na restart aplikacji
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "GoogleApiClient connection has been suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopLocationUpdates();
        stopSelf();
    }
    private void stopLocationUpdates() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
            //TODO: logika przy zarejestrowanej zmianie lokalizacji
        System.out.println("accuracy: "+location.getAccuracy());
        System.out.println("x: "+location.getLongitude());
        System.out.println("y: "+ location.getLatitude());
        System.out.println("bearing: "+ location.getBearing());
        FirebaseDataAnalyzeService.saveData(new Alarm(location.getLongitude(),location.getLatitude()));
       // NotificationService.buildNotification(location.getLatitude(),location.getLongitude());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void buildNotification() {
        String textTitle = "AlarMe";
        String textContent = "Praca w tle";

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyApplication.getContext());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyApplication.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
// notificationId is a unique int for each notification that you must define
         notificationID =(int) (Math.random()*100);
        notificationManager.notify(notificationID, mBuilder.build());
    }

    public static void cancelNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyApplication.getContext());
        notificationManager.cancelAll();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        cancelNotification();
    }
}
