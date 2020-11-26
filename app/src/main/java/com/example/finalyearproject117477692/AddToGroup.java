package com.example.finalyearproject117477692;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddToGroup extends AppCompatActivity {
    EditText etName, etAge, etContact;
    Button btnSave, btnShow;
    DatabaseReference reff;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_group);

        //Learned from Michael Gleeson Android lecture 
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null){
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected){
            try{
                Toast.makeText(AddToGroup.this, "Network is connected", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(AddToGroup.this, "Network is not connected", Toast.LENGTH_SHORT).show();
        }
        // https://www.youtube.com/watch?v=iy6WexahCdY Reference for adding to database
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etContact = findViewById(R.id.etContact);
        btnSave = findViewById(R.id.btnSave);
        btnShow = findViewById(R.id.btnShow);
        member = new Member();
        reff = FirebaseDatabase.getInstance().getReference().child("GroupMembers");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etName.getText().toString().isEmpty()){
                    etName.setError("Error");
                }else if(etAge.getText().toString().isEmpty()){
                    etAge.setError("Error");
                }else if(etContact.getText().toString().isEmpty()){
                    etContact.setError("Error");
                }
                else{
                    int agea = Integer.parseInt(etAge.getText().toString().trim());
                    member.setName(etName.getText().toString().trim());
                    member.setAge(agea);
                    member.setContact(etContact.getText().toString().trim());


                    reff.push().setValue(member);

                    Toast.makeText(AddToGroup.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();

                    //Clearing text boxes once button pressed
                    etName.setText("");
                    etAge.setText("");
                    etContact.setText("");
                    etName.requestFocus();
                }
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddToGroup.this, ShowMembers.class);
                startActivity(intent);
            }
        });
    }
}