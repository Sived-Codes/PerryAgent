package com.prashant.backgroundcallername.BackgroundServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class BackgroundBroadcastService extends Service {
    public BackgroundBroadcastService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try{
            Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
            restartServiceIntent.setPackage(getPackageName());
            startService(restartServiceIntent);

            super.onTaskRemoved(rootIntent);
        }catch (Exception e){
            Log.d("Background Service:", e.getMessage());

        }

    }

}
