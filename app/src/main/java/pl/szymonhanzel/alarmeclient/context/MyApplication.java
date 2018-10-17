package pl.szymonhanzel.alarmeclient.context;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static final String SHARED_PREFERENCES_NAME = "MyPref";
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }
    public static String getSharedPreferencesName() {
        return SHARED_PREFERENCES_NAME;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
