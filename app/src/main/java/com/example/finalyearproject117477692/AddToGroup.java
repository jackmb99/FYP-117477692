package com.example.finalyearproject117477692;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddToGroup extends AppCompatActivity {
    EditText editTextFirstName, editTextLastName, editTextAge;
    Button button;
    DatabaseReference reff;
    Member member;

    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_group);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), ShowMembers.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), ShowGoals.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
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


        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        button = findViewById(R.id.button);
        member = new Member();
        reff = FirebaseDatabase.getInstance().getReference().child("GroupMembers");
        // https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
        // Above is source for the below email validation pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


       // initUI();
        // setButtonOnClickListener();
        /*handleBundle();
        initUIFromPerson();*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextAge.getText().toString().trim();
                if (email.matches(emailPattern)){
                    if(editTextFirstName.getText().toString().isEmpty()){
                        editTextFirstName.setError("Error");
                    }else if(editTextLastName.getText().toString().isEmpty()){
                        editTextLastName.setError("Error");
                    }else if(editTextAge.getText().toString().isEmpty()){
                        editTextAge.setError("Error");
                    }else{
                        int agea = Integer.parseInt(editTextLastName.getText().toString().trim());
                        member.setName(editTextFirstName.getText().toString().trim());
                        member.setAge(agea);
                        member.setContact(editTextAge.getText().toString().trim());

                        // reff.push().setValue(member);

                   /* String key = reff.push().getKey();
                    member.setKey(key);
                    reff.child(key).setValue(member);*/

                        if(edit){
                            reff.child(member.getKey()).setValue(member);
                        }else{
                            String key = reff.push().getKey();
                            member.setKey(key);
                            reff.child(key).setValue(member);
                            Toast.makeText(AddToGroup.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                        }
                        finish();

                    }
                }else{
                    editTextAge.setError("Invalid email");
                }

            }
        });
    }

//    private void initUI(){
//        editTextFirstName = findViewById(R.id.etName);
//        editTextLastName = findViewById(R.id.etAge);
//        editTextAge = findViewById(R.id.etContact);
//        button = findViewById(R.id.btnSave);
//    }

    /*private void initUIFromPerson(){
        editTextFirstName.setText(member.getName());
        editTextLastName.setText(member.getContact());
        editTextAge.setText(member.getAge() + "");
    }*/

    /*private void setButtonOnClickListener(){
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
    }*/


  /*  private void handleBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            edit = bundle.getBoolean("edit");
            if(edit){
                member = Parcels.unwrap(bundle.getParcelable("member"));
            }
        }
    }*/
}