package pl.szymonhanzel.alarmeclient.context;

import android.app.Application;
import android.content.Context;
import android.location.Location;

import com.google.firebase.firestore.FirebaseFirestore;

public class MyApplication extends Application {
    private static final String SHARED_PREFERENCES_NAME = "MyPref";
    private static Application sApplication;
    private static   FirebaseFirestore dbReference;
    private static String token;




    public static FirebaseFirestore getDb(){
        return dbReference;
    }
    public static void setDb(FirebaseFirestore db){
        dbReference=db;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }
    public static String getSharedPreferencesName() {
        return SHARED_PREFERENCES_NAME;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        MyApplication.token = token;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
