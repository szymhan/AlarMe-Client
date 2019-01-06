package pl.szymonhanzel.alarmeclient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.szymonhanzel.alarmeclient.component.DatabaseAdapter;
import pl.szymonhanzel.alarmeclient.enumerator.VehicleEnum;

public class AlarmConfirmationActivity extends AppCompatActivity {

    private LinearLayout mainLinearLayout;
    private TextView distanceTextView, addressTextView;
    private ImageView vehicleTypeImageView;
    private MediaPlayer mPlayer;
    private Button finishActivityButton;

    private final String TAG = "AlarmConfirmationActvt";
    private String vehicleType;
    private int distance;
    String address;
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private static final String ALARM_SOUND_KEY = "sound";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_confirmation);
        //TODO: pobranie danych z Intent ...
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final Intent intent = getIntent();
        vehicleType = intent.getStringExtra("vehicleType");
        distance = intent.getIntExtra("distance",1000);
        address = intent.getStringExtra("address");


       assignFrontElements();
       changeBackgroundColor(vehicleType);
       setDistanceTextView(distance);
       setAddressTextView(address);

        vibrateAndPlaySound();
        addEventToDatabase();
    }


    private void assignFrontElements() {
        mainLinearLayout = findViewById(R.id.alarm_confirmation_background);
        distanceTextView = findViewById(R.id.alarm_confirmation_distance);
        vehicleTypeImageView = findViewById(R.id.alarm_confirmation_vehicle);
        addressTextView = findViewById(R.id.alarm_confirmation_address);
        finishActivityButton = findViewById(R.id.alarm_confirmation_close_button);
        finishActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void vibrateAndPlaySound() {
        //============== URUCHOMIENIE WIBRACJI NA 3 SEKUNDY ============================
            Vibrator v = (Vibrator) this.getSystemService(getApplicationContext().VIBRATOR_SERVICE);
            v.vibrate(3000);

            SharedPreferences sp = getSharedPreferences("MyPref",0);
            if(sp.getBoolean(ALARM_SOUND_KEY,true)){
                mPlayer = MediaPlayer.create(this,R.raw.alarm);
                mPlayer.start();
            }

    }

    private void changeBackgroundColor(String vehicleType) {
        vehicleTypeImageView.setImageResource(0);
        switch (vehicleType){
            case ("Pogotowie"):
                mainLinearLayout.setBackgroundColor(Color.parseColor(VehicleEnum.AMBULANCE.getColor()));
                vehicleTypeImageView.setImageResource(R.drawable.ambulance);

                break;
            case "Straż pożarna":
                mainLinearLayout.setBackgroundColor(Color.parseColor(VehicleEnum.FIRE_BRIGADE.getColor()));
                vehicleTypeImageView.setImageResource(R.drawable.firefighter);
                break;
            case "Policja" :
                mainLinearLayout.setBackgroundColor(Color.parseColor(VehicleEnum.POLICE.getColor()));
                vehicleTypeImageView.setImageResource(R.drawable.police_car);
        }

    }

    private void setDistanceTextView(int distance) {
        String textToInsert = distance + " m";
        distanceTextView.setText(textToInsert);
    }

    private void setAddressTextView(String address){
        addressTextView.setText(address);
    }

    private void addEventToDatabase() {
        Date date = new Date();
        DatabaseAdapter dbAdapter = new DatabaseAdapter(this);
        dbAdapter.open();
        dbAdapter.insert(vehicleType,address, sdf.format(date));
        dbAdapter.close();

    }
}
