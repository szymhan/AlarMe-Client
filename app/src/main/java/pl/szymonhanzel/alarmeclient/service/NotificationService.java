package pl.szymonhanzel.alarmeclient.service;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import pl.szymonhanzel.alarmeclient.R;
import pl.szymonhanzel.alarmeclient.context.MyContext;

public class NotificationService {

    private static final String TAG = "NotificationService";
    private static final String CHANNEL_ID = "1996";
    //latitude - szerokosc geo (N/S), longitude - długość geo (W/E)

    public static void buildAppNotWorkingNotification() {
        String textTitle = "AlarMe";
        String textContent = "Usługa wyłączona";

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyContext.getContext());
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyContext.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
// notificationId is a unique int for each notification that you must define
        int notificationID =(int) (Math.random()*100);
        notificationManager.notify(notificationID, mBuilder.build());
    }



    public static void cancelNotifications() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyContext.getContext());
        notificationManager.cancelAll();
        NotificationService.buildAppNotWorkingNotification();
    }

}
