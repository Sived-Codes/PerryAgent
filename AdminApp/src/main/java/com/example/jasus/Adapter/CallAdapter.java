package com.example.jasus.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasus.Model.CallModel;
import com.example.jasus.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class CallAdapter extends FirebaseRecyclerAdapter<CallModel, CallAdapter.ViewHolder> {


    public CallAdapter(@NonNull FirebaseRecyclerOptions<CallModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CallAdapter.ViewHolder holder, int position, @NonNull CallModel model) {

        holder.state.setText(model.getState());


        holder.number.setText(model.getIncomingnumber());


        holder.time.setText(model.getDate());

    }

    @NonNull
    @Override
    public CallAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_layout, parent, false);
        return new ViewHolder(view)  ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

          TextView state,number,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            state=itemView.findViewById(R.id.state);
            number=itemView.findViewById(R.id.incomningnumber);
            time=itemView.findViewById(R.id.Date);
        }

    }
}
