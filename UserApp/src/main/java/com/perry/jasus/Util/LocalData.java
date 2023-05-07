package com.perry.jasus.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.perry.jasus.Ui.MainActivity;

public class LocalData {
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    public static void saveDeviceID (Context context, String deviceId){

        preferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("deviceId", deviceId);
        editor.commit();

    }

    public static String  getDeviceID (){
        String deviceID = preferences.getString("deviceId", "");
        return deviceID;
    }

    public static void  check (Context context){
        preferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);

        if (preferences.contains("deviceId")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }

    }

}
