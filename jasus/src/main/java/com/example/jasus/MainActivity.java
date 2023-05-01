package com.example.jasus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Jasus_Adapter adapter;
    ArrayList<Call_jasus>List;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Raksha");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerview);

        List=new ArrayList<>();
       recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        FirebaseRecyclerOptions<Call_jasus> options
                = new FirebaseRecyclerOptions.Builder<Call_jasus>()
                .setQuery(reference, Call_jasus.class)
                .build();
        adapter=new Jasus_Adapter(options);
       recyclerView.setAdapter(adapter);

    }
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}