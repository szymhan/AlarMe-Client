package pl.szymonhanzel.alarmeclient.service;

import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MyFirebaseMessagingServ";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //TODO: reakcja na otrzymanie wiadomości

        System.out.println(remoteMessage.getData());

        //anulowanie wszystkich notyfikacji
        //TODO: PODJĘCIE REAKCJI NA WYSTĘPUJĄCY ALARM
    }
    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
