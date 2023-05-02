package com.prashant.backgroundcallername.BroadcastServices;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prashant.backgroundcallername.BackgroundServices.BackgroundBroadcastService;
import com.prashant.backgroundcallername.Database.DatabaseHelper;
import com.prashant.backgroundcallername.LocalData;
import com.prashant.backgroundcallername.Models.Call_Model;

import java.util.Calendar;
import java.util.Date;


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
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            DatabaseHelper db = new DatabaseHelper(context);
            Date currentTime = Calendar.getInstance().getTime();
            String date = String.valueOf(currentTime);


            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                if (incomingNumber!=null){
                    Call_Model model = new Call_Model("Incomming", incomingNumber, date);
                    String key = reference.push().getKey();
                    Toast.makeText(context,"Call Aa raha hai Number : "+incomingNumber,Toast.LENGTH_SHORT).show();
                    reference.child(key).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    db.addCallDetails(state, incomingNumber, String.valueOf(currentTime));
                }


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


