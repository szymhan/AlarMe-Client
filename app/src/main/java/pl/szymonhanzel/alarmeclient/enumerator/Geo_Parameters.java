package pl.szymonhanzel.alarmeclient.enumerator;

public enum Geo_Parameters {
    ALTITUDE("altitude"),
    LONGITUDE("longitude"),
    LATITUDE("latitude");

    private String parameter;

     Geo_Parameters(String parameter){
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

}
