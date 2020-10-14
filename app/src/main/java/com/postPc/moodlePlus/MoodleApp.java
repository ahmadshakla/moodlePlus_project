package com.postPc.moodlePlus;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MoodleApp extends Application {
    public final static String CHANNEL_ID = "Notification Channel";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "Calender Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("A chanel for the calender events");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);

        }

    }
}
