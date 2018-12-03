package pl.szymonhanzel.alarmeclient;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import pl.szymonhanzel.alarmeclient.component.PermissionChecker;
import pl.szymonhanzel.alarmeclient.fragment.AlarMeFragment;
import pl.szymonhanzel.alarmeclient.enumerator.NavigationEnum;
import pl.szymonhanzel.alarmeclient.fragment.HistoryFragment;
import pl.szymonhanzel.alarmeclient.fragment.SettingsFragment;
import pl.szymonhanzel.alarmeclient.service.GPSService;

public class MainActivity extends AppCompatActivity {


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
                case R.id.navigation_history:
                    initFragment(NavigationEnum.HISTORY);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        initNavigationBar();
        Intent gpsService =new Intent(getApplicationContext(),GPSService.class);
        getApplicationContext().startService(gpsService);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkPermissions() {
        if (!PermissionChecker.isGPSEnabled(getApplicationContext())){
            showGPSEnablingDialog();
        }
    }


    private void initNavigationBar() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
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
            case HISTORY:
                ft.replace(R.id.container, new HistoryFragment());
                break;
        }

        ft.commit();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        checkPermissions();
    }

    private void showGPSEnablingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.gps_not_found_title);  // GPS not found
        builder.setMessage(R.string.gps_not_found_message); // Want to enable?
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GPSService.cancelNotifications();
    }
}
