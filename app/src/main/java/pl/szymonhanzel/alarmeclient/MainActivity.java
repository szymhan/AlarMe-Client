package pl.szymonhanzel.alarmeclient;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.szymonhanzel.alarmeclient.fragment.AlarMeFragment;
import pl.szymonhanzel.alarmeclient.enumerator.NavigationEnum;
import pl.szymonhanzel.alarmeclient.fragment.SettingsFragment;
import pl.szymonhanzel.alarmeclient.service.FirebaseDataAnalyzeService;

public class MainActivity extends AppCompatActivity {


    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("alarms");

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   initFragment(NavigationEnum.HOME);
                    return true;
                case R.id.navigation_dashboard:
                    initFragment(NavigationEnum.SETTINGS);
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            FirebaseDataAnalyzeService.analyzeData(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private OnSuccessListener<Location> locationListener = new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if (location!=null) {
                FirebaseDataAnalyzeService.lastKnownLocation=location;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(locationListener);
        }catch (SecurityException se){
            se.printStackTrace();
        }

        initNavigationBar();
        myRef.addValueEventListener(valueEventListener);
    }


    private void initNavigationBar() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragment(NavigationEnum.HOME);
    }

    private void initFragment(NavigationEnum ne) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (ne){
            case HOME:
                ft.replace(R.id.container,new AlarMeFragment());
                break;
            case SETTINGS:
                ft.replace(R.id.container, new SettingsFragment());
                break;
        }

        ft.commit();
    }
}
