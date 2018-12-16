package pl.szymonhanzel.alarmeclient.service;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import pl.szymonhanzel.alarmeclient.context.MyApplication;
import pl.szymonhanzel.alarmeclient.model.Alarm;

public class FirebaseDataAnalyzeService {

    private static final String TAG = "FirebaseDataAnalyzeServ";
    private static Location lastKnownLocation;


    public static Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public static void setLastKnownLocation(Location lastKnownLocation) {
        FirebaseDataAnalyzeService.lastKnownLocation = lastKnownLocation;
    }

    public static void saveData(Alarm alarm){
        MyApplication.getDb()
                .collection("users")
                .add(alarm)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"Dokument został dodany z ID:" + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Wystąpił błąd podczas dodawania dokumentu do bazy");
                    }
                });
    }

    public static void saveData(List<Location> locations){
        if(!locations.isEmpty()){
            for (Location location: locations){
                Alarm alarmToSave = new Alarm(location.getLongitude(),location.getLatitude(),location.getAltitude());
                saveData(alarmToSave);
            }
        }
    }
}
