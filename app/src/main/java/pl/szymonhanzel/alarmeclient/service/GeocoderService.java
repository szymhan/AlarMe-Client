package pl.szymonhanzel.alarmeclient.service;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pl.szymonhanzel.alarmeclient.context.MyContext;

public class GeocoderService {

    private static final String TAG = "GeocoderService";
    private static final String UNKNOWN_ADDRESS = "Nieznany adres";

    public static String getAddress(double latitude, double longitude) {
        Geocoder gcd = new Geocoder(MyContext.getContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getLocality();
            }
            else {
                Log.d(TAG, "Addresses list is empty. returning default value...");
                return UNKNOWN_ADDRESS;
            }
        } catch (IOException ioe){
            Log.d(TAG, "Impossible to get address from latitude and longitude.");
            return UNKNOWN_ADDRESS;
        }

    }
}
