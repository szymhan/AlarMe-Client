package pl.szymonhanzel.alarmeclient.service;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.context.MyApplication;

public class NotificationService {

    private static final String TAG = "NotificationService";
    private static final String CHANNEL_ID = "1995";
    //latitude - szerokosc geo (N/S), longitude - długość geo (W/E)
    public static void buildNotification(double latitude, double longitude) {
        String textTitle = "AlarMe";
        String textContent = "Szerokość:" + latitude +", Długość: "+longitude;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyApplication.getContext());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyApplication.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
// notificationId is a unique int for each notification that you must define
        int notificationId =(int) (Math.random()*100);
        notificationManager.notify(notificationId, mBuilder.build());
    }
}
