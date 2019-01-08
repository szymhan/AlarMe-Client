package pl.szymonhanzel.alarmeclient.component;

import android.location.Location;

import pl.szymonhanzel.alarmeclient.model.RemoteMessageDataModel;

public class DistanceCalculator {




    /**
     * Wzór obliczający odległość pomiędzy dwoma punktami na podstawie
     * metody Haversine'a. jako wysokość przekazywana jest wartość zero,
     * aby aby brać pod uwagę jedynie szerokość i wysokość geograficzną
     *
     * @returns Dystans w metrach
     */
    public static int distance(Location location, RemoteMessageDataModel rmdm) {

        double lat1 = location.getLatitude();
        double lon1 = location.getLongitude();
        double el1 = 0;
        double lat2 = rmdm.getLatitude();
        double lon2 = rmdm.getLongitude();
        double el2 = 0;

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return (int) Math.sqrt(distance);
    }
}
