package pl.szymonhanzel.alarmeclient.model;

public class SQLAlarmModel {

    private String vehicleType;
    private String address;
    private String date;

    public SQLAlarmModel(String vehicleType, String address, String date) {
        this.vehicleType = vehicleType;
        this.address = address;
        this.date = date;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
