package pl.szymonhanzel.alarmeclient.component;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

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
}
