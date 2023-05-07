package com.perry.admin.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.perry.admin.Model.ContactModel;
import com.perry.admin.Model.ContactModel;
import com.perry.admin.R;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class ContactAdapter extends FirebaseRecyclerAdapter<ContactModel, ContactAdapter.ViewHolder> {


    public ContactAdapter(@NonNull FirebaseRecyclerOptions<ContactModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position, @NonNull ContactModel model) {

        holder.name.setText(model.getName());
        holder.number.setText(model.getMobile());

    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_layout, parent, false);
        return new ViewHolder(view)  ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

          TextView name,number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            number=itemView.findViewById(R.id.contact_number);
            name=itemView.findViewById(R.id.contact_name);
        }

    }
}
