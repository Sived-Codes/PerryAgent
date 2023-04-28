package com.prashant.backgroundcallername.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashant.backgroundcallername.Fragments.CallFragment;
import com.prashant.backgroundcallername.Models.Call_Model;
import com.prashant.backgroundcallername.R;

import java.util.ArrayList;

public class CallAdpater extends RecyclerView.Adapter<CallAdpater.ViewHolder> {
    ArrayList<Call_Model>cList;
    Context context;

    public CallAdpater(ArrayList<Call_Model> cList, Context context) {
        this.cList = cList;
        this.context = context;
    }

    @NonNull
    @Override
    public CallAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.call_activity,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdpater.ViewHolder holder, int position) {

        holder.state.setText(cList.get(position).getState());
        holder.incomingnumber.setText(cList.get(position).getIncomingnumber());
        holder.date.setText(cList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return cList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView state,incomingnumber,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            state=itemView.findViewById(R.id.state);
            incomingnumber=itemView.findViewById(R.id.incomningnumber);
            date=itemView.findViewById(R.id.Date);
        }
    }
}
