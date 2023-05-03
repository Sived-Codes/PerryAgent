package com.prashant.backgroundcallername;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class LocalData {
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    public static void saveDeviceID (Context context, String deviceId){

        preferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("deviceId", deviceId);
        editor.commit();

    }

    public static String  getDeviceID (Context context){
        preferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);
        String deviceID = preferences.getString("deviceId", "");
        return deviceID;
    }

    public static void  check (Context context){
        preferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);

        if (preferences.contains("deviceId")){
            context.startActivity(new Intent(context, MainActivity.class));
        }

    }

}
