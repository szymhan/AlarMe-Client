package pl.szymonhanzel.alarmeclient.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import pl.szymonhanzel.alarmeclient.MainActivity;
import pl.szymonhanzel.alarmeclient.R;

/**
 * SplashScreen jest używany do wywołania podstawowych metod i usług w tle, by poprawić
 * efekt wizualny ładowania aplikacji. Tutaj używana jest metoda getFirebaseToken do otrzymania
 * tokena umożliwiającego połączenie się z usługą Cloud Messaging od Firebase
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFirebaseToken();
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
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                    //    Toast.makeText(SplashScreenActivity.this, msg, Toast.LENGTH_SHORT).show();
                        FirebaseMessaging.getInstance().subscribeToTopic("alarms");
                    }
                });

    }
}
