package com.prashant.backgroundcallername.BroadcastServices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.prashant.backgroundcallername.Database.DatabaseHelper;
import com.prashant.backgroundcallername.Database.SMS_Database;

import java.util.Calendar;
import java.util.Date;

public class IncomingSms extends IncomingCall {
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Working " +"Sender Num : "+ senderNum + ", Msg: " + message+",currentTime :", duration);
                    toast.show();


                    SMS_Database smsdb = new SMS_Database(context);
                    Date currentTime = Calendar.getInstance().getTime();
                    smsdb.addSMSDetails(message,senderNum, String.valueOf(currentTime));

                    Log.i("PRASHANT145", "=========  "  +senderNum);


                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
