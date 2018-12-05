package pl.szymonhanzel.alarmeclient.enumerator;

public enum VehicleEnum {
    POLICE("P","#00a8ff"),
    AMBULANCE("A","#f5f6fa"),
    FIRE_BRIGADE("F","#e84118");


    String symbol;
    String color;

    VehicleEnum(String symbol,String color){
        this.symbol = symbol;
        this.color = color;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getColor() {
        return color;
    }
}
