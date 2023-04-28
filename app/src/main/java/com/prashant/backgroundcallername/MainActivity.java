package com.prashant.backgroundcallername;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.prashant.backgroundcallername.BackgroundServices.BackgroundBroadcastService;
import com.prashant.backgroundcallername.BackgroundServices.ReStarter;
import com.prashant.backgroundcallername.BackgroundServices.BackgroundService;
import com.prashant.backgroundcallername.Fragments.CallFragment;
import com.prashant.backgroundcallername.Fragments.SMSFragment;
import com.prashant.backgroundcallername.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Intent mServiceIntent;
    //TextView sms;
    private BackgroundService mBackgroundService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sms=findViewById(R.id.sms);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestPermissions();
        runner();
        //Attach in Incoming Call & Incoming SMS
        startService(new Intent(this, BackgroundBroadcastService.class));

        //Create Service
        mBackgroundService = new BackgroundService();
        mServiceIntent = new Intent(this, mBackgroundService.getClass());
        if (!isMyServiceRunning(mBackgroundService.getClass())) {
            startService(mServiceIntent);
        }




        replaceFragment(new CallFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.contact_me) {

                replaceFragment(new CallFragment());
            }
            if (item.getItemId() == R.id.SMS_me) {
                replaceFragment(new SMSFragment());

            }

            return true;
        });

    }

        private void replaceFragment(Fragment fragment){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,fragment);
            fragmentTransaction.commit();

    }

    //Current Service Checker
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    //Background Toast
    private void runner() {
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                runner();
//                Toast.makeText(MainActivity.this, "Running in background...!", Toast.LENGTH_SHORT).show();
            }

        }.start();
    }

    //Run time Permission
    private void requestPermissions() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.READ_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Toast.makeText(MainActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }else{
                            finish();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                    
                }).withErrorListener(error -> {
                    Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                .onSameThread().check();
    }

    //Run time Permission Dialog
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {

            dialog.cancel();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
        broadcastIntent.setClass(this, ReStarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}