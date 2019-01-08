package pl.szymonhanzel.alarmeclient.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pl.szymonhanzel.alarmeclient.AlarmConfirmationActivity;
import pl.szymonhanzel.alarmeclient.component.DistanceCalculator;
import pl.szymonhanzel.alarmeclient.context.MyContext;
import pl.szymonhanzel.alarmeclient.model.RemoteMessageDataModel;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MyFirebaseMessagingServ";
    private static final String NOTIFICATIONS_KEY = "notifications";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //TODO: reakcja na otrzymanie wiadomości

        System.out.println(remoteMessage.getData());

        //anulowanie wszystkich notyfikacji
        //TODO: PODJĘCIE REAKCJI NA WYSTĘPUJĄCY ALARM
        if(FirebaseDataAnalyzeService.validateAlarm(remoteMessage.getData())){
            Log.d(TAG,"Message: " + remoteMessage.getFrom() + "validated!");
            System.out.println("Message: "+ remoteMessage.getFrom() + " validated!");
            RemoteMessageDataModel rmdm = new RemoteMessageDataModel(remoteMessage.getData());
            Location location = FirebaseDataAnalyzeService.getLastKnownLocation();
            SharedPreferences preferences = MyContext.getContext().getSharedPreferences("MyPref",0);
            boolean isNotifyingEnabled = preferences.getBoolean(NOTIFICATIONS_KEY,true);
            int distance =DistanceCalculator.distance(location,rmdm);
            if(distance<MyContext.DISTANCE && isNotifyingEnabled){
                FirebaseDataAnalyzeService.convertVehicleTypeName(rmdm);
                //TODO: oppznic wykonanie tego fragmentu
                    Intent intent = new Intent(MyContext.getContext(),AlarmConfirmationActivity.class);
                    intent.putExtra("vehicleType",rmdm.getVehicleType());
                    intent.putExtra("address",rmdm.getAddress());
                    intent.putExtra("distance",distance);
                    startActivity(intent);
            }
        }
    }
    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
