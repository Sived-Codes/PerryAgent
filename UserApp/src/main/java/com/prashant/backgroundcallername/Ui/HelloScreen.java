package com.prashant.backgroundcallername.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.prashant.backgroundcallername.Util.LocalData;
import com.prashant.backgroundcallername.Models.UserModel;
import com.prashant.backgroundcallername.R;

public class HelloScreen extends AppCompatActivity {

    EditText name;
    Button startNow;

    ProgressDialog pd;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_screen);

        pd = new ProgressDialog(this);
        pd.setMessage("Please wait ...");


        LocalData.check(HelloScreen.this);
        name = findViewById(R.id.name_edittext);
        startNow = findViewById(R.id.start_btn);

        startNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                String getName = name.getText().toString();

                if (getName.isEmpty()){
                    pd.dismiss();
                    Toast.makeText(HelloScreen.this, "Please enter your name !!", Toast.LENGTH_SHORT).show();
                }else{

                    String model = Build.MODEL + " " + android.os.Build.BRAND +" (" + android.os.Build.VERSION.RELEASE+")";
                    String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                    UserModel model1 = new UserModel(getName, model, deviceId);
                    reference.child(deviceId).setValue(model1).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            LocalData.saveDeviceID(HelloScreen.this, deviceId);
                            Toast.makeText(HelloScreen.this, "Welcome " +getName, Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            Intent intent = new Intent(HelloScreen.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(HelloScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}