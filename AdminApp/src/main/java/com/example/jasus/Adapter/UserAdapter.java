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
                LocalData.saveDeviceID(v.getContext(), model.getUserDeviceId());
                v.getContext().startActivity(intent);
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

        TextView name, model, deviceId;
        MaterialCardView user;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.user_name);
            deviceId=itemView.findViewById(R.id.user_deviceId);
            model=itemView.findViewById(R.id.user_mobile);
            user=itemView.findViewById(R.id.user);


        }
    }
}
