package com.example.jasus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasus.R;
import com.example.jasus.Model.SmsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class SmsAdapter extends FirebaseRecyclerAdapter<SmsModel, SmsAdapter.ViewHolder> {



    public SmsAdapter(@NonNull FirebaseRecyclerOptions<SmsModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, int position, @NonNull SmsModel model) {
        holder.sender.setText(model.getSendernum());
        holder.msg.setText(model.getMessage());
        holder.ctime.setText(model.getCurrentTime());
    }

    @NonNull
    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_layout, parent, false);
        return new SmsAdapter.ViewHolder(view)  ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sender, msg, ctime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sender=itemView.findViewById(R.id.sender_num);
            msg=itemView.findViewById(R.id.message_t);
            ctime=itemView.findViewById(R.id.current_time);


        }
    }
}
