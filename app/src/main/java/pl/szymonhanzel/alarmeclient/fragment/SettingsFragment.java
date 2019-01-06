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

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.context.MyContext;


public class SettingsFragment extends Fragment {

    private static final String NOTIFICATIONS_KEY = "notifications";
    private static final String ALARM_SOUND_KEY = "sound";
    Switch notificationsSwitch, soundSwitch;

    private Switch.OnCheckedChangeListener onCheckedChangeListener = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            saveDataInSharedPreferences(NOTIFICATIONS_KEY,isChecked);
            if(isChecked){
                soundSwitch.setClickable(true);
                soundSwitch.setChecked(isSwitchEnabled(ALARM_SOUND_KEY));
            } else {
                soundSwitch.setClickable(false);
                saveSoundSwitchValueAndSetCheckedToFalse();
            }
        }
    };

    private Switch.OnCheckedChangeListener alarmSoundSwitchCheckedListener = new Switch.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            saveDataInSharedPreferences(ALARM_SOUND_KEY,isChecked);
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
       soundSwitch = view.findViewById(R.id.fragment_settings_sound_switch);
        SharedPreferences sp = MyContext.getContext().getSharedPreferences(MyContext.getSharedPreferencesName(),0);
        boolean isNotificationEnabled = sp.getBoolean(NOTIFICATIONS_KEY,true);
        boolean isAlarmSoundEnabled = isNotificationEnabled && sp.getBoolean(ALARM_SOUND_KEY, true);
        notificationsSwitch.setChecked(isNotificationEnabled);
        soundSwitch.setChecked(isAlarmSoundEnabled);
        soundSwitch.setClickable(isNotificationEnabled);
        notificationsSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
        soundSwitch.setOnCheckedChangeListener(alarmSoundSwitchCheckedListener);
    }

    private void saveDataInSharedPreferences(String key, boolean value) {
        SharedPreferences sp = MyContext.getContext().getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    private boolean isSwitchEnabled(String key) {
        SharedPreferences sp = MyContext.getContext().getSharedPreferences(MyContext.getSharedPreferencesName(),0);
        boolean isAlarmSoundEnabled = sp.getBoolean(key,true);
        return isAlarmSoundEnabled;
    }

    private void saveSoundSwitchValueAndSetCheckedToFalse() {
        boolean isChecked = soundSwitch.isChecked();
        soundSwitch.setChecked(false);
        saveDataInSharedPreferences(ALARM_SOUND_KEY,isChecked);
    }
}
