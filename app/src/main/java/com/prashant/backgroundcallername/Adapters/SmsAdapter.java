package com.prashant.backgroundcallername.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashant.backgroundcallername.Models.Call_Model;
import com.prashant.backgroundcallername.Models.SMS_Model;
import com.prashant.backgroundcallername.R;

import java.util.ArrayList;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {

    ArrayList<SMS_Model> sList;
    Context context;

    public SmsAdapter(ArrayList<SMS_Model> sList, Context context) {
        this.sList = sList;
        this.context = context;
    }

    @NonNull
    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_activity,parent,false);
        return  new SmsAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, int position) {
holder.number.setText(sList.get(position).getSendernum());
        holder.msg.setText(sList.get(position).getMessage());
        holder.time.setText(sList.get(position).getCurrentTime());
    }

    @Override
    public int getItemCount() {
        return sList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number,msg,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.sender_num);
            msg=itemView.findViewById(R.id.message_t);
            time=itemView.findViewById(R.id.current_time);


        }
    }
}
