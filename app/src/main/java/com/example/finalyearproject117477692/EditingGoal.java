package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

// Code from Michael Gleesons CRUD on firebase

public class EditingGoal extends AppCompatActivity {

    // declare variables
    private EditText editTextFirstName;
    private EditText editTextAge;
    private Button button, btnCancel;
    ActionBar actionBar;

    private DatabaseReference databaseReference;

    private Goal goal = new Goal();

    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_goal);

        // action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Update Goal");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.about);

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

        databaseReference = FirebaseDatabase.getInstance().getReference("IndividualGoals");

        initUI();
        setButtonOnClickListener();
        handleBundle();
        initUIFromPerson();
        setCancelButtonOnClickListener();
    }
    // initialising interface
    private void initUI(){
        editTextFirstName = findViewById(R.id.editTextFirstName);

        editTextAge = findViewById(R.id.editTextAge);
        button = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void initUIFromPerson(){
        editTextFirstName.setText(goal.getTitle());

        editTextAge.setText(goal.getDescription());
    }

    // setting text of fields to data being edited and saving new data
    private void setButtonOnClickListener(){
        button.setOnClickListener(e -> {

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String title = editTextFirstName.getText().toString();
            String description = editTextAge.getText().toString();

            goal.setTitle(title);

            goal.setDescription(description);

            goal.setUserUid(uid);

            if(edit){
                databaseReference.child(goal.getKey()).setValue(goal);
            }else{
                String key = databaseReference.push().getKey();
                goal.setKey(key);
                databaseReference.child(key).setValue(goal);
            }
            finish();
        });
    }
    private void setCancelButtonOnClickListener(){
        btnCancel.setOnClickListener(e -> {
            Intent intent = new Intent(EditingGoal.this, ShowGoals.class);
            startActivity(intent);
        });
    }

        // edit
    private void handleBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            edit = bundle.getBoolean("edit");
            if(edit){
                goal = Parcels.unwrap(bundle.getParcelable("goal"));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
        // log out
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(EditingGoal.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
            case R.id.menuHome:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return true;
    }

}