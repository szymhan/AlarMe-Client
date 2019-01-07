package pl.szymonhanzel.alarmeclient.context;

import android.app.Application;
import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Klasa zadeklarowana w manifeście, pozwalająca na przekazywanie
 * kontekstu aplikacji do klas nie będąych aktywnościami/fragmentami
 */
public class MyContext extends Application {
    private static final String SHARED_PREFERENCES_NAME = "MyPref";
    private static Application sApplication;
    private static   FirebaseFirestore dbReference;
    private static String token;
    private static FirebaseMessaging messagingReference;
    public static final double DISTANCE = 1000;


    public static double getDISTANCE() {
        return DISTANCE;
    }

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
        MyContext.token = token;
    }

    public static FirebaseMessaging getMessagingReference() {
        return messagingReference;
    }

    public static void setMessagingReference(FirebaseMessaging messagingReference) {
        MyContext.messagingReference = messagingReference;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
