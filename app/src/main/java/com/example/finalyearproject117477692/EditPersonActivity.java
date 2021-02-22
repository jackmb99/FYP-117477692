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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

// Code from Michael Gleesons CRUD on firebase

public class EditPersonActivity extends AppCompatActivity {

    // declare variables
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

        databaseReference = FirebaseDatabase.getInstance().getReference("GroupMembers");

        initUI();
        setButtonOnClickListener();
        handleBundle();
        initUIFromPerson();
        setCancelButtonOnClickListener();
    }
    // initialise interface
    private void initUI(){
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        button = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
    }
    // data being edited being set to text fields
    private void initUIFromPerson(){
        editTextFirstName.setText(member.getName());
        editTextLastName.setText(member.getContact());
        editTextAge.setText(member.getAge() + "");
    }
    // saving edited data
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
                Toast.makeText(EditPersonActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
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
