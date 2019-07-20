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
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
//        Toast.makeText(getApplicationContext(),"Got a notification! ",Toast.LENGTH_SHORT).show();
        if(sharedPreferences.getBoolean("status",true)) {
            Toast.makeText(getApplicationContext(),"get and status true!",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"get and status false!",Toast.LENGTH_LONG).show();
        }
        if(!sharedPreferences.getBoolean("status",true)) {
            return;
        }

        if(isQueuing)
            alarmManager.cancel(pendingIntent);
        Intent intent = new Intent(getApplication().getApplicationContext(),PlayNotificationSoundService.class);
        pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,0);

        int delay=sharedPreferences.getInt("delay",3000);
//        Toast.makeText(getApplicationContext(),Integer.toString(delay),Toast.LENGTH_LONG).show();

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