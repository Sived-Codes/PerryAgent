package com.example.jasus.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasus.Util.LocalData;
import com.example.jasus.Model.UserModel;
import com.example.jasus.R;
import com.example.jasus.Ui.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAdapter extends FirebaseRecyclerAdapter<UserModel, UserAdapter.ViewHolder> {



    public UserAdapter(@NonNull FirebaseRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position, @NonNull UserModel model) {
        holder.name.setText(model.getUserName());
        holder.model.setText(model.getUserMobile());
        holder.deviceId.setText(model.getUserDeviceId());

        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                LocalData.saveDeviceID(v.getContext(), model.getUserDeviceId(), model.getUserName());
                v.getContext().startActivity(intent);
            }
        });
        DatabaseReference callDb = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUserDeviceId()).child("Call");
        DatabaseReference smsDb = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getUserDeviceId()).child("Sms");

        smsDb.addValueEventListener(new ValueEventListener() {
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
        callDb.addValueEventListener(new ValueEventListener() {
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


    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new UserAdapter.ViewHolder(view)  ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, model, deviceId, userTotalMsg, userTotalCall;
        MaterialCardView user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.user_name);
            deviceId=itemView.findViewById(R.id.user_deviceId);
            model=itemView.findViewById(R.id.user_mobile);
            user=itemView.findViewById(R.id.user);
            userTotalMsg=itemView.findViewById(R.id.userTotalMsg);
            userTotalCall=itemView.findViewById(R.id.userTotalCall);


        }
    }
}
