package pl.szymonhanzel.alarmeclient.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.messaging.FirebaseMessaging;

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.context.MyApplication;


public class SettingsFragment extends Fragment {

    private static final String NOTIFICATIONS_KEY = "notifications";
    Switch notificationsSwitch;

    private Switch.OnCheckedChangeListener onCheckedChangeListener = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences sp = MyApplication.getContext().getSharedPreferences("MyPref",0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(NOTIFICATIONS_KEY,isChecked);
            editor.apply();
            if(isChecked){
                FirebaseMessaging.getInstance().subscribeToTopic("alarms");
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("alarms");
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       notificationsSwitch = view.findViewById(R.id.fragment_settings_notifications_switch);
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(MyApplication.getSharedPreferencesName(),0);
        boolean isNotificationEnabled = sp.getBoolean(NOTIFICATIONS_KEY,true);
        notificationsSwitch.setChecked(isNotificationEnabled);
        notificationsSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}
