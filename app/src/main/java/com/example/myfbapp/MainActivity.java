package com.example.myfbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button mBtnSave, mBtnView;
    EditText mName, mEmail, mPassword;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnSave = findViewById(R.id.btnSave);
        mBtnView = findViewById(R.id.btnView);
        mName = findViewById(R.id.edtName);
        mEmail = findViewById(R.id.edtMail);
        mPassword = findViewById(R.id.edtPass);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Saving");
        dialog.setMessage("Please wait...");

        //Start saving after someone has clicked on the btn save
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Receive data from the user
                String jina = mName.getText().toString();
                String arafa = mEmail.getText().toString();
                String siri = mPassword.getText().toString().trim();
                //We will require a unique ID to store in our column ID
                long time = System.currentTimeMillis();
                //Because our columns can only store strings, convert time to string
                String timeConverted = String.valueOf(time);

                //Check if the data inputs are empty
                if (jina.isEmpty() || arafa.isEmpty() || siri.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fill all inputs", Toast.LENGTH_SHORT).show();
                }else {
                    //Proceed to store data
                    //Start by creating a child/table
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users/"+timeConverted);
                    //Invoke your Item class to create the columns in the child/table
                    Item x = new Item(jina,arafa,siri,timeConverted);

                    //Finally push your data into the db
                    //Show the prograss dialog as it is saving
                    dialog.show();
                    ref.setValue(x).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "User saved successfully", Toast.LENGTH_SHORT).show();
                                mName.setText("");
                                mEmail.setText("");
                                mPassword.setText("");
                            }else {
                                Toast.makeText(MainActivity.this, "Saving failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });
        //To view data, allow the user to go to a new activity which has a fetching code
        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UsersActivity.class));
            }
        });
    }
}
