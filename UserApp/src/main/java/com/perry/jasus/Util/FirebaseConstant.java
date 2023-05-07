package com.perry.jasus.Util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConstant {

    static String deviceId = LocalData.getDeviceID();
    public static DatabaseReference UserDb = FirebaseDatabase.getInstance().getReference().child("Users");
    public static DatabaseReference UserDeviceDb = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId);
    public static DatabaseReference ContactDb = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId).child("Contacts");
    public static DatabaseReference CallHistoryDb = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId).child("Call History");
    public static DatabaseReference SmsDb = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId).child("Sms");
    public static DatabaseReference PhotoDb = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId).child("Photos");
}
