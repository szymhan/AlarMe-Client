package pl.szymonhanzel.alarmeclient.enumerator;

import android.drm.DrmStore;

import pl.szymonhanzel.alarmeclient.R;

public enum DirectionEnum {
    EAST("E", R.drawable.e_direction),
    WEST("W",R.drawable.w_direction),
    NORTH("N",R.drawable.n_direction),
    SOUTH("S",R.drawable.s_direction),
    SOUTHERN_EAST("SE",R.drawable.se_direction),
    NORTHERN_EASE("NE",R.drawable.ne_direction),
    NORTHERN_WEST("NW",R.drawable.nw_direction),
    SOUTHERN_WEST("SW",R.drawable.sw_direction);

    private String direction;
    private int referenceImage;

     DirectionEnum(String direction,int referenceImage){
        this.direction = direction;
        this.referenceImage=referenceImage;
    }

    public String getDirection() {
        return direction;
    }

    public int getReferenceImage() {
        return referenceImage;
    }
}
