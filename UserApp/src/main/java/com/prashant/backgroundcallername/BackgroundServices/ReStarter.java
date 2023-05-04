package com.prashant.backgroundcallername.BackgroundServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class ReStarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            Toast.makeText(context, "Service Restarted", Toast.LENGTH_SHORT).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, BackgroundService.class));
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                context.startForegroundService(new Intent(context, BackgroundService.class));
            }

            else {
                context.startService(new Intent(context, BackgroundService.class));
            }

        }catch (Exception e){
            Log.d("ReStarter Error : ", e.getMessage());
        }

    }
}