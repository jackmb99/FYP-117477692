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

public class EditGoalActivity extends AppCompatActivity {
    EditText etTitle, etDescription;
    Button button;
    DatabaseReference reff;
    Goal goal;

    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.about);

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
                Toast.makeText(EditGoalActivity.this, "Network is connected", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(EditGoalActivity.this, "Network is not connected", Toast.LENGTH_SHORT).show();
        }

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);

        button = findViewById(R.id.button);
        goal = new Goal();
        reff = FirebaseDatabase.getInstance().getReference().child("IndividualGoals");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etTitle.getText().toString().isEmpty()){
                    etTitle.setError("Error");
                }else if(etDescription.getText().toString().isEmpty()){
                    etDescription.setError("Error");
                }else{

                    goal.setTitle(etTitle.getText().toString().trim());

                    goal.setDescription(etDescription.getText().toString().trim());

                    // reff.push().setValue(member);

                   /* String key = reff.push().getKey();
                    member.setKey(key);
                    reff.child(key).setValue(member);*/

                    if(edit){
                        reff.child(goal.getKey()).setValue(goal);
                    }else{
                        String key = reff.push().getKey();
                        goal.setKey(key);
                        reff.child(key).setValue(goal);
                        Toast.makeText(EditGoalActivity.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    }
                    finish();

                }
            }
        });
    }
}