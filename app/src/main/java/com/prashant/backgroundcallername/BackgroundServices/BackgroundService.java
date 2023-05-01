package com.prashant.backgroundcallername.BackgroundServices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {
    public int counter=0;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU){
            startMyOwnForeground();
        }

        if  (Build.VERSION.SDK_INT >Build.VERSION_CODES.O){
            startMyOwnForeground();
        }else{
            startForeground(1, new Notification());
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "channel";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setPriority(NotificationManager.IMPORTANCE_UNSPECIFIED)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .build();
        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimerTask();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
//        broadcastIntent.setClass(this, ReStarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    public void startTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Log.i("Count", "=========  " + (counter++));
            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
