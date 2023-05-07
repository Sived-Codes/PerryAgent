package com.perry.admin.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.perry.admin.Adapter.CallAdapter;
import com.perry.admin.Adapter.ContactAdapter;
import com.perry.admin.Model.CallModel;
import com.perry.admin.Model.ContactModel;
import com.perry.admin.R;
import com.perry.admin.Util.LocalData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ContactFragment extends Fragment {
    RecyclerView recyclerView;
    ContactAdapter adapter;

    FloatingActionButton contactSave;


    DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_contact, container, false);

        recyclerView = view.findViewById(R.id.contact_recyclerview);
        contactSave = view.findViewById(R.id.contact_save);

        TextView name = view.findViewById(R.id.user_Name_callF);

        String deviceId = LocalData.getDeviceID(getContext());
        String userName = LocalData.getName(getContext());
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(deviceId).child("contacts");

        contactSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndRetrieveDataLocally();
            }
        });

        name.setText(userName);

        if (!deviceId.isEmpty()){

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int size = (int) snapshot.getChildrenCount();

                    TextView calls = view.findViewById(R.id.total_contact);
                    String c = String.valueOf(size);
                    calls.setText("(" + c + ")");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            FirebaseRecyclerOptions<ContactModel> options
                    = new FirebaseRecyclerOptions.Builder<ContactModel>()
                    .setQuery(reference, ContactModel.class)
                    .build();

            adapter = new ContactAdapter(options);
            recyclerView.setAdapter(adapter);
        }


        return view;
    }

    private void saveAndRetrieveDataLocally() {


        // Read the data from Firebase
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    // Write the data to a file on your device
                    File file = new File(getActivity().getFilesDir(), "contact.json");
                    FileWriter writer = new FileWriter(file);
                    writer.write(dataSnapshot.getValue().toString());
                    writer.close();
                    System.out.println("Data saved to file successfully.");
                    Toast.makeText(getContext(), "Data saved to file successfully", Toast.LENGTH_SHORT).show();
                    // Read the data from the file
                    Log.i("FilePath", "File path: " + file.getAbsolutePath());
                    Toast.makeText(getContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String jsonString = reader.readLine();
                    reader.close();

                    // Do something with the data
                    System.out.println("Retrieved data: " + jsonString);
                } catch (IOException e) {
                    System.out.println("Error saving or retrieving data: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Error reading data from Firebase: " + databaseError.getMessage());
            }
        });
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