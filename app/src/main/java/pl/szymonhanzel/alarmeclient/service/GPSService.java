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
import android.util.Log;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.Nullable;



public class GPSService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{


    private static final String TAG = "GPSService";
    private static int PERMISSION_ACCESS_FINE_LOCATION = 1;

    private boolean currentlyProcessingLocation = false;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient mLocationProviderClient;

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
        locationRequest.setFastestInterval(1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException se) {
            Log.e(TAG, "Go into settings and find Gps Tracker app and enable Location.");
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
        NotificationService.buildNotification(location.getLatitude(),location.getLongitude());
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
