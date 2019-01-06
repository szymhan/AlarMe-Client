package pl.szymonhanzel.alarmeclient.model;

import com.google.firebase.Timestamp;

import java.util.Map;

import pl.szymonhanzel.alarmeclient.enumerator.Geo_ParametersEnum;
import pl.szymonhanzel.alarmeclient.service.GeocoderService;

public class RemoteMessageDataModel {

    private double altitude,latitude,longitude;
    private String address, actualDay,vehicleType;

    public RemoteMessageDataModel(Map<String,String> valuesMap){

        altitude = Double.parseDouble(valuesMap.get(Geo_ParametersEnum.ALTITUDE.getParameter()));
        longitude = Double.parseDouble(valuesMap.get(Geo_ParametersEnum.LONGITUDE.getParameter()));
        latitude = Double.parseDouble(valuesMap.get(Geo_ParametersEnum.LATITUDE.getParameter()));
        address = GeocoderService.getAddress(latitude,longitude);
        actualDay = Timestamp.now().toString();
        vehicleType = valuesMap.get("vehicleType");
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActualDay() {
        return actualDay;
    }

    public void setActualDay(String actualDay) {
        this.actualDay = actualDay;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
