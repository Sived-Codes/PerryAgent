package com.example.jasus.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jasus.Adapter.CallAdapter;
import com.example.jasus.Util.LocalData;
import com.example.jasus.Model.CallModel;
import com.example.jasus.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CallFragment extends Fragment {


    RecyclerView recyclerView;
    CallAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_call, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);

        TextView name = view.findViewById(R.id.user_Name_callF);

        String deviceId = LocalData.getDeviceID(getContext());
        String userName = LocalData.getName(getContext());

        name.setText(userName);

        if (!deviceId.isEmpty()){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId).child("Call");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int size = (int) snapshot.getChildrenCount();

                    TextView calls = view.findViewById(R.id.total_call);
                    String c = String.valueOf(size);
                    calls.setText("(" + c + ")");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            FirebaseRecyclerOptions<CallModel> options
                    = new FirebaseRecyclerOptions.Builder<CallModel>()
                    .setQuery(reference, CallModel.class)
                    .build();

            adapter = new CallAdapter(options);
            recyclerView.setAdapter(adapter);
        }


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}