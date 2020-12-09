package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

public class EditingGoal extends AppCompatActivity {
    private EditText editTextFirstName;
    private EditText editTextAge;
    private Button button, btnCancel;

    private DatabaseReference databaseReference;

    private Goal goal = new Goal();

    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing_goal);

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

        databaseReference = FirebaseDatabase.getInstance().getReference("IndividualGoals");

        initUI();
        setButtonOnClickListener();
        handleBundle();
        initUIFromPerson();
        setCancelButtonOnClickListener();
    }
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

    private void setButtonOnClickListener(){
        button.setOnClickListener(e -> {
            String title = editTextFirstName.getText().toString();
            String description = editTextAge.getText().toString();

            goal.setTitle(title);

            goal.setDescription(description);

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


    private void handleBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            edit = bundle.getBoolean("edit");
            if(edit){
                goal = Parcels.unwrap(bundle.getParcelable("goal"));
            }
        }
    }
}