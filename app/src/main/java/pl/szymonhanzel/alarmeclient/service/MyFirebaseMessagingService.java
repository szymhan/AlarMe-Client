package pl.szymonhanzel.alarmeclient.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MyFirebaseMessagingServ";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //TODO: reakcja na otrzymanie wiadomości



        //anulowanie wszystkich notyfikacji
        GPSService.cancelNotifications();
    }
    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
