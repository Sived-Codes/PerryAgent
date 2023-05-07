package com.perry.admin;

import com.google.firebase.database.*;
import java.io.*;

public class FirebaseToLocal {

    public static void main(String[] args) {

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("path/to/child");

        // Read the data from Firebase
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    // Write the data to a file on your computer
                    File file = new File("contacts.json");
                    FileWriter writer = new FileWriter(file);
                    writer.write(dataSnapshot.getValue().toString());
                    writer.close();
                    System.out.println("Data saved to file successfully.");

                    // Read the data from the file
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
}
