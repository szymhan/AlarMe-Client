package pl.szymonhanzel.alarmeclient.model;

public class Alarm {

    private Direction direction;




    private enum Direction {
        EAST,
        WEST,
        NORTH,
        SOUTH,
        EASTERN_SOUTH,
        EASTERN_NORTH,
        WESTERN_NORTH,
        WESTERN_SOUTH
    }
}
