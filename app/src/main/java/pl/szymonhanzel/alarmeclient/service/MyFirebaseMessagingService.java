package pl.szymonhanzel.alarmeclient.service;

import android.content.Intent;
import android.location.Location;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pl.szymonhanzel.alarmeclient.AlarmConfirmationActivity;
import pl.szymonhanzel.alarmeclient.component.DatabaseAdapter;
import pl.szymonhanzel.alarmeclient.context.MyApplication;
import pl.szymonhanzel.alarmeclient.model.RemoteMessageDataModel;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG = "MyFirebaseMessagingServ";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        //TODO: reakcja na otrzymanie wiadomości

        System.out.println(remoteMessage.getData());

        //anulowanie wszystkich notyfikacji
        //TODO: PODJĘCIE REAKCJI NA WYSTĘPUJĄCY ALARM
        if(FirebaseDataAnalyzeService.validateAlarm(remoteMessage.getData())){
            RemoteMessageDataModel rmdm = new RemoteMessageDataModel(remoteMessage.getData());
            Location location = FirebaseDataAnalyzeService.getLastKnownLocation();
            double distance = DistanceBearingCalculatorService.distance(
                    location.getLatitude(),
                    rmdm.getLatitude(),
                    location.getLongitude(),
                    rmdm.getLongitude(),
                    0,0);
            if(distance<MyApplication.getDISTANCE()){
                DatabaseAdapter databaseAdapter = new DatabaseAdapter(MyApplication.getContext()).open();
                databaseAdapter.insert(rmdm.getVehicleType(),rmdm.getAddress(),rmdm.getActualDay());
                Intent intent = new Intent(MyApplication.getContext(),AlarmConfirmationActivity.class);
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
