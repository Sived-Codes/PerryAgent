package com.example.jasus.Util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.jasus.Ui.MainActivity;

public class LocalData {
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    public static void saveDeviceID (Context context, String deviceId, String name){

        preferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("deviceId", deviceId);
        editor.putString("name", name);
        editor.commit();

    }

    public static String  getDeviceID (Context context){
        preferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);
        String deviceID = preferences.getString("deviceId", "");
        return deviceID;
    }

    public static String  getName (Context context){
        preferences = context.getSharedPreferences("users", Context.MODE_PRIVATE);
        String name = preferences.getString("name", "");
        return name;
    }



}
