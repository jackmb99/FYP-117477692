package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

/**
 * Created by Michael Gleeson on 03/12/2020
 * Copyright (c) 2020
 */

public class EditPersonActivity extends AppCompatActivity {
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private Button button, btnCancel;

    private DatabaseReference databaseReference;

    private Member member = new Member();

    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        databaseReference = FirebaseDatabase.getInstance().getReference("GroupMembers");

        initUI();
        setButtonOnClickListener();
        handleBundle();
        initUIFromPerson();
        setCancelButtonOnClickListener();
    }

    private void initUI(){
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        button = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void initUIFromPerson(){
        editTextFirstName.setText(member.getName());
        editTextLastName.setText(member.getContact());
        editTextAge.setText(member.getAge() + "");
    }

    private void setButtonOnClickListener(){
        button.setOnClickListener(e -> {
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            int age = Integer.parseInt(editTextAge.getText().toString());

            member.setName(firstName);
            member.setContact(lastName);
            member.setAge(age);

            if(edit){
                databaseReference.child(member.getKey()).setValue(member);
            }else{
                String key = databaseReference.push().getKey();
                member.setKey(key);
                databaseReference.child(key).setValue(member);
            }
            finish();
        });
    }
    private void setCancelButtonOnClickListener(){
        btnCancel.setOnClickListener(e -> {
            Intent intent = new Intent(EditPersonActivity.this, ShowMembers.class);
            startActivity(intent);
        });
    }

    private void handleBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            edit = bundle.getBoolean("edit");
            if(edit){
                member = Parcels.unwrap(bundle.getParcelable("member"));
            }
        }
    }
}
