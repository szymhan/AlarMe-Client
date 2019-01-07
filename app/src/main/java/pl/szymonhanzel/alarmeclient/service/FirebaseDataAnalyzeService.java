package pl.szymonhanzel.alarmeclient.service;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;

import pl.szymonhanzel.alarmeclient.context.MyContext;
import pl.szymonhanzel.alarmeclient.model.Alarm;
import pl.szymonhanzel.alarmeclient.model.RemoteMessageDataModel;


public class FirebaseDataAnalyzeService {

    private static final String TAG = "FirebaseDataAnalyzeServ";
    private static Location lastKnownLocation;
    private static String vehicleTypeName;
    private static Map<String, Object> vehicleTypesMap;

    public static Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public static void setLastKnownLocation(Location lastKnownLocation) {
        FirebaseDataAnalyzeService.lastKnownLocation = lastKnownLocation;
    }

    public static void saveData(Alarm alarm) {
        MyContext.getDb()
                .collection("users")
                .add(alarm)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Dokument został dodany z ID:" + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Wystąpił błąd podczas dodawania dokumentu do bazy");
                    }
                });
    }

    public static void saveData(List<Location> locations) {
        if (!locations.isEmpty()) {
            for (Location location : locations) {
                Alarm alarmToSave = new Alarm(location.getLongitude(), location.getLatitude(), location.getAltitude());
                saveData(alarmToSave);
            }
        }
    }

    public static boolean validateAlarm(Map<String, String> valuesMap) {
        return valuesMap.containsKey("altitude")
                && valuesMap.containsKey("latitude")
                && valuesMap.containsKey("vehicleType")
                && valuesMap.containsKey("longitude");

    }

    public static void updateVehicleTypesMap() {
        DocumentReference docRef = MyContext.getDb().collection("vehicles").document("vehicles");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    vehicleTypesMap = documentSnapshot.getData();
                }
            }
        });
    }

    public static void convertVehicleTypeName(RemoteMessageDataModel rmdm) {
        updateVehicleTypesMap();
        try {
            if(vehicleTypesMap.containsKey(rmdm.getVehicleType())){
                vehicleTypeName = vehicleTypesMap.get(rmdm.getVehicleType()).toString();
            } else {
                vehicleTypeName = vehicleTypesMap.get("0").toString();
            }
            rmdm.setVehicleType(vehicleTypeName);

        } catch (Exception e){
            Log.e(TAG,"Getting vehicle type failed");
        }
    }
}
