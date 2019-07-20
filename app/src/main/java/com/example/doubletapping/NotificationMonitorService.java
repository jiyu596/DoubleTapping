package com.example.doubletapping;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;
import java.lang.Integer.*;


@SuppressLint("NewApi")
public class NotificationMonitorService extends NotificationListenerService {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private boolean isQueuing = false;

    @Override
    public void onCreate() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        Toast.makeText(getApplicationContext(),"Got a notification!",Toast.LENGTH_LONG).show();

        if(isQueuing)
            alarmManager.cancel(pendingIntent);
        Intent intent = new Intent(getApplication().getApplicationContext(),PlayNotificationSoundService.class);
        pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,0);

//        getApplicationContext().getSharedPreferences(, Context.MODE_PRIVATE).getInt("delay",3000);
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int delay=sharedPreferences.getInt("delay",3000);
        Toast.makeText(getApplicationContext(),Integer.toString(delay),Toast.LENGTH_LONG).show();

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+delay,pendingIntent);
        isQueuing = true;
    }

    // 在删除消息时触发
    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        if(isQueuing) {
            alarmManager.cancel(pendingIntent);
        }
        isQueuing = false;
    }
}