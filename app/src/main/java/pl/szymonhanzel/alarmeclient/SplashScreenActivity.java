package pl.szymonhanzel.alarmeclient;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import pl.szymonhanzel.alarmeclient.context.MyContext;

/**
 * SplashScreen jest używany do wywołania podstawowych metod i usług w tle, by poprawić
 * efekt wizualny ładowania aplikacji. Tutaj używana jest metoda getFirebaseToken do otrzymania
 * tokena umożliwiającego połączenie się z usługą Cloud Messaging od Firebase
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final String CHANNEL_ID = "1996";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        getFirebaseToken();
        createNotificationChannel();
        MyContext.setDb(FirebaseFirestore.getInstance());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //method used to get Firebase token to be able to connect to Cloud Messaging platform
    private void getFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        MyContext.setToken(token);
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                    //    Toast.makeText(SplashScreenActivity.this, msg, Toast.LENGTH_SHORT).show();
                        MyContext.setMessagingReference(FirebaseMessaging.getInstance());
                        MyContext.getMessagingReference().subscribeToTopic("alarms");
                        System.out.println("Firebase Messaging AutoInitEnabled :"
                                + MyContext.getMessagingReference().isAutoInitEnabled());
                    }
                });

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new a i9nd not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
