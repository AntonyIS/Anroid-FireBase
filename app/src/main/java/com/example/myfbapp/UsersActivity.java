package com.example.myfbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {
    ListView list;
    CustomAdapter adapter;
    ArrayList<Item> users;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        list = findViewById(R.id.listYangu);
        users = new ArrayList<>();
        adapter = new CustomAdapter(this,users);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Please wait...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        //Start listening to the data as it comes in
        // Show the dialog as you fetch data
        dialog.show();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Clear the arraylist befor starting to stack it up with data from the db
                users.clear();
                //Loop to get data from the db
                for (DataSnapshot snap:dataSnapshot.getChildren()){
                    Item x = snap.getValue(Item.class);
                    //Add the received data that is now on variable x to your arraylist
                    users.add(x);

                }
                //Notify the adapter that data has changed
                adapter.notifyDataSetChanged();
                //Dismiss the dialog
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //If the database is locked
                //May be the rules in the db are set to false and false
                Toast.makeText(UsersActivity.this, "Sorry, Db locked. Contact the dev team to sort this", Toast.LENGTH_SHORT).show();
            }
        });
        //Finally set the list to the adapter
        list.setAdapter(adapter);
    }
}
