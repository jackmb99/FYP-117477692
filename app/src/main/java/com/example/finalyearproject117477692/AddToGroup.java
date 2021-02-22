package com.example.finalyearproject117477692;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


// Code from Michael Gleesons CRUD on firebase
// insert from https://www.youtube.com/watch?v=iy6WexahCdY

public class AddToGroup extends AppCompatActivity {
    // declare variables
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

        /*Bottom nav code from https://www.youtube.com/watch?v=JjfSjMs0ImQ*/
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
        // checks network connectivity
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validation
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

                        if(edit){
                            reff.child(member.getKey()).setValue(member);
                        }else{
                            // writing to database
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


  @Override
  public boolean onCreateOptionsMenu(Menu menu){

      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu, menu);
      return true;
  }
        // log out functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(AddToGroup.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
    }
}