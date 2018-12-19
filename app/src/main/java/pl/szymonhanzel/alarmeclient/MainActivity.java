package pl.szymonhanzel.alarmeclient;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import pl.szymonhanzel.alarmeclient.component.PermissionChecker;
import pl.szymonhanzel.alarmeclient.fragment.AlarMeFragment;
import pl.szymonhanzel.alarmeclient.enumerator.NavigationEnum;
import pl.szymonhanzel.alarmeclient.fragment.HistoryFragment;
import pl.szymonhanzel.alarmeclient.fragment.SettingsFragment;
import pl.szymonhanzel.alarmeclient.service.GPSUpdatesLocationService;
import pl.szymonhanzel.alarmeclient.service.MyFirebaseMessagingService;
import pl.szymonhanzel.alarmeclient.service.NotificationService;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener{

    private static final long UPDATE_INTERVAL = 10 * 1000;
    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "MainActivity";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   initFragment(NavigationEnum.HOME);
                    return true;
                case R.id.navigation_dashboard:
                    initFragment(NavigationEnum.SETTINGS);
                    return true;
                case R.id.navigation_history:
                    initFragment(NavigationEnum.HISTORY);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        initNavigationBar();
        buildGoogleApiClient();
       // Intent gpsService =new Intent(getApplicationContext(),GPSService.class);
        //getApplicationContext().startService(gpsService);
        Intent messagingService = new Intent(getApplicationContext(),MyFirebaseMessagingService.class);
        getApplicationContext().startService(messagingService);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkPermissions() {
        if (!PermissionChecker.isGPSEnabled(getApplicationContext()) || !PermissionChecker.permissionGPSPermissionGranted()){
            showGPSEnablingDialog();
        }
    }


    private void initNavigationBar() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragment(NavigationEnum.HOME);
    }

    private void initFragment(NavigationEnum ne) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (ne){
            case HOME:
                ft.replace(R.id.container,new AlarMeFragment());
                break;
            case SETTINGS:
                ft.replace(R.id.container, new SettingsFragment());
                break;
            case HISTORY:
                ft.replace(R.id.container, new HistoryFragment());
                break;
        }

        ft.commit();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        checkPermissions();
    }

    private void showGPSEnablingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.gps_not_found_title);  // GPS not found
        builder.setMessage(R.string.gps_not_found_message); // Want to enable?
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Aplikacja wymaga GPS do działania.",Toast.LENGTH_SHORT).show();
                stopGPSService();
                dialog.cancel();
         //       System.exit(0);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void stopGPSService() {
    //TODO: w przypadku negatywnej odpowiedzi powinniśmy coś zrobić z aplikacją (zamknięcie/ notyfikacja?)
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationService.cancelNotifications();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        final String text = "Connection suspended";
        Log.w(TAG, text + ": Error code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        final String text = "Exception while connecting to Google Play services";
        Log.w(TAG, text + ": " + connectionResult.getErrorMessage());

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }

    public void requestLocationUpdates() {
        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            Log.e(TAG," Unable to request location updates in method requestLocationUpdates()");
        }
    }

    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        }
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, GPSUpdatesLocationService.class);
        intent.setAction(GPSUpdatesLocationService.ACTION_PROCESS_UPDATES);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void removeLocationUpdates(View view) {
        Log.i(TAG, "Removing location updates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                getPendingIntent());
    }
}
