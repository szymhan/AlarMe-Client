package pl.szymonhanzel.alarmeclient;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.szymonhanzel.alarmeclient.enumerator.VehicleEnum;

public class AlarmConfirmationActivity extends AppCompatActivity {

    LinearLayout mainLinearLayout;
    TextView distanceTextView;
    ImageView vehicleTypeImageView;
    ImageView directionImageView;

    String vehicleType;
    String direction;
    int distance;

    private static final String AMBULANCE = VehicleEnum.AMBULANCE.getSymbol();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_confirmation);
        //TODO: pobranie danych z Intent ...

       assignFrontElements();
       changeBackgroundColor(vehicleType);

    }


    private void assignFrontElements() {
        mainLinearLayout = findViewById(R.id.alarm_confirmation_background);
        distanceTextView = findViewById(R.id.alarm_confirmation_distance);
        vehicleTypeImageView = findViewById(R.id.alarm_confirmation_vehicle);
        directionImageView = findViewById(R.id.alarm_confirmation_direction);
    }



    private void changeBackgroundColor(String vehicleType) {

        switch (vehicleType){
            case ("A"):
                mainLinearLayout.setBackgroundColor(Color.parseColor(VehicleEnum.AMBULANCE.getColor()));
                break;
            case "F":
                mainLinearLayout.setBackgroundColor(Color.parseColor(VehicleEnum.FIRE_BRIGADE.getColor()));
                break;
            case "P" :
                mainLinearLayout.setBackgroundColor(Color.parseColor(VehicleEnum.POLICE.getColor()));
        }

    }

    private void changeDirectionImage(String direction) {

    }
}
