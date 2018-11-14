package pl.szymonhanzel.alarmeclient.component;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.context.MyApplication;

public class PermissionChecker {

    public static boolean permissionGranted() {
        if (ContextCompat.checkSelfPermission(MyApplication.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED ) {
           return false;
        } else {
            return true;
        }
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return false;
        }
        return true;
    }


}
