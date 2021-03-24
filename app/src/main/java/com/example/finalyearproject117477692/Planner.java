package com.example.finalyearproject117477692;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Planner extends AppCompatActivity {

    private EditText etMonday, etTuesday, etWednesday, etThursday, etFriday, etSaturday, etSunday;
    private Button btnPlanner;
    Plan plan;
    DatabaseReference reff;
    private ProgressBar progressBar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Group Planner");

        btnPlanner = findViewById(R.id.btnPlanner);
        etMonday = findViewById(R.id.etMonday);
        etTuesday = findViewById(R.id.etTuesday);
        etWednesday = findViewById(R.id.etWednesday);
        etThursday = findViewById(R.id.etThursday);
        etFriday = findViewById(R.id.etFriday);
        etSaturday = findViewById(R.id.etSaturday);
        etSunday = findViewById(R.id.etSunday);

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            btnPlanner.setVisibility(View.GONE);
            etMonday.setEnabled(false);
            etTuesday.setEnabled(false);
            etWednesday.setEnabled(false);
            etThursday.setEnabled(false);
            etFriday.setEnabled(false);
            etSaturday.setEnabled(false);
            etSunday.setEnabled(false);

            etMonday.setTypeface(null, Typeface.BOLD);
            etTuesday.setTypeface(null, Typeface.BOLD);
            etWednesday.setTypeface(null, Typeface.BOLD);
            etThursday.setTypeface(null, Typeface.BOLD);
            etFriday.setTypeface(null, Typeface.BOLD);
            etSaturday.setTypeface(null, Typeface.BOLD);
            etSunday.setTypeface(null, Typeface.BOLD);

            etMonday.setTextColor(Color.parseColor("#000000"));
            etTuesday.setTextColor(Color.parseColor("#000000"));
            etWednesday.setTextColor(Color.parseColor("#000000"));
            etThursday.setTextColor(Color.parseColor("#000000"));
            etFriday.setTextColor(Color.parseColor("#000000"));
            etSaturday.setTextColor(Color.parseColor("#000000"));
            etSunday.setTextColor(Color.parseColor("#000000"));

        }



        progressBar = findViewById(R.id.progressBar);

        // assign database reff
        reff = FirebaseDatabase.getInstance().getReference().child("Planner").child("1");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String monday = snapshot.child("monday").getValue().toString();
                String tuesday = snapshot.child("tuesday").getValue().toString();
                String wednesday = snapshot.child("wednesday").getValue().toString();
                String thursday = snapshot.child("thursday").getValue().toString();
                String friday = snapshot.child("friday").getValue().toString();
                String saturday = snapshot.child("saturday").getValue().toString();
                String sunday = snapshot.child("sunday").getValue().toString();

                etMonday.setText(monday);
                etTuesday.setText(tuesday);
                etWednesday.setText(wednesday);
                etThursday.setText(thursday);
                etFriday.setText(friday);
                etSaturday.setText(saturday);
                etSunday.setText(sunday);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // initialize class
        plan = new Plan();
        btnPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMonday.getText().toString().isEmpty()){
                    etMonday.setError("Error");
                }else if(etTuesday.getText().toString().isEmpty()){
                    etTuesday.setError("Error");
                }else if(etWednesday.getText().toString().isEmpty()){
                    etWednesday.setError("Error");
                }else if(etThursday.getText().toString().isEmpty()){
                    etThursday.setError("Error");
                }else if(etFriday.getText().toString().isEmpty()){
                    etFriday.setError("Error");
                }else if(etSaturday.getText().toString().isEmpty()){
                    etSaturday.setError("Error");
                }else if(etSunday.getText().toString().isEmpty()){
                    etSunday.setError("Error");
                }else{
                    plan.setMonday(etMonday.getText().toString().trim());
                    plan.setTuesday(etTuesday.getText().toString().trim());
                    plan.setWednesday(etWednesday.getText().toString().trim());
                    plan.setThursday(etThursday.getText().toString().trim());
                    plan.setFriday(etFriday.getText().toString().trim());
                    plan.setSaturday(etSaturday.getText().toString().trim());
                    plan.setSunday(etSunday.getText().toString().trim());

                    /*String key = reff.push().getKey();
                    plan.setKey(key);*/
                    reff.setValue(plan);
                    Toast.makeText(Planner.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu2, menu);
        return true;
    }

    // log out
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(Planner.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
    }
}