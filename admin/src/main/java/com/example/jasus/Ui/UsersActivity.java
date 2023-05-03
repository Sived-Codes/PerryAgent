package com.example.jasus.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.jasus.Adapter.SmsAdapter;
import com.example.jasus.Adapter.UserAdapter;
import com.example.jasus.Model.SmsModel;
import com.example.jasus.Model.UserModel;
import com.example.jasus.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    UserAdapter adapter;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.user_recycler);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = (int) snapshot.getChildrenCount();

                TextView user = findViewById(R.id.total_user);
                String c = String.valueOf(size);
                user.setText("(" + c + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        FirebaseRecyclerOptions<UserModel> options
                = new FirebaseRecyclerOptions.Builder<UserModel>()
                .setQuery(reference, UserModel.class)
                .build();

        adapter = new UserAdapter(options);
        recyclerView.setAdapter(adapter);

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