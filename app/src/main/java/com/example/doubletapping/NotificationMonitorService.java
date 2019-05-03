package com.example.doubletapping;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;



@SuppressLint("NewApi")
public class NotificationMonitorService extends NotificationListenerService {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private boolean isqueuing = false;

    @Override
    public void onCreate() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    // 在收到消息时触发
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        // Toast.makeText(getApplicationContext(),"25",Toast.LENGTH_LONG).show();
        if(isqueuing)
            alarmManager.cancel(pendingIntent);
        Intent intent = new Intent(getApplication().getApplicationContext(),PlayNotificationSoundService.class);
        pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+1000*3,pendingIntent);
        isqueuing = true;
    }

    // 在删除消息时触发
    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification) {
        if(isqueuing) {
            alarmManager.cancel(pendingIntent);
        }
        isqueuing = false;
    }
}