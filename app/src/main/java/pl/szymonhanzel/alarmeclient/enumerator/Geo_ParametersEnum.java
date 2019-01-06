package pl.szymonhanzel.alarmeclient.enumerator;

public enum Geo_ParametersEnum {
    ALTITUDE("altitude"),
    LONGITUDE("longitude"),
    LATITUDE("latitude");

    private String parameter;

     Geo_ParametersEnum(String parameter){
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }

}
