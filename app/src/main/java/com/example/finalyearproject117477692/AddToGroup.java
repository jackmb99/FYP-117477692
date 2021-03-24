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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


// Code from Michael Gleesons CRUD on firebase
// insert from https://www.youtube.com/watch?v=iy6WexahCdY

public class AddToGroup extends AppCompatActivity {
    // declare variables
    EditText editTextFirstName, editTextLastName, editTextAge;
    Button button;
    DatabaseReference reff;
    Member member;
    ActionBar actionBar;
    public static String distance;


    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_group);

        // action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("New Individual Exercise");

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("Distance");

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // here you can get your data from this snapshot object
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    distance = dataSnapshot.child(uid).child("distance").getValue().toString();
                    System.out.println(distance);
                }

                /*String data = dataSnapshot.getValue("distance").toString();*/

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        editTextFirstName = findViewById(R.id.editTextFirstName);
      //  editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        button = findViewById(R.id.button);
        member = new Member();
        reff = FirebaseDatabase.getInstance().getReference().child("IndividualExercise");

        // https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext
        // Above is source for the below email validation pattern
     //   String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email2 = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                double distanceTravelled = Double.parseDouble(distance);
                // validation
                String email = editTextAge.getText().toString().trim();

                    if(editTextFirstName.getText().toString().isEmpty()){
                        editTextFirstName.setError("Error");
                    }else if(editTextAge.getText().toString().isEmpty()){
                        editTextAge.setError("Error");
                    }else{

                        String dbDistance = String.valueOf(editTextAge.getText());
                        double semiDistance = Double.parseDouble(dbDistance);

                        double finalDistance = semiDistance + distanceTravelled;
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        int agea = Integer.parseInt(editTextAge.getText().toString().trim());
                        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                        member.setName(editTextFirstName.getText().toString().trim());
                        member.setContact(agea);
                        member.setUserUid(uid);
                        member.setDateStamp(timeStamp);

                        HashMap map = new HashMap();
                        map.put("name", email2);
                        map.put("distance", finalDistance);
                        map.put("userUid", uid);

                        FirebaseDatabase.getInstance().getReference("Distance").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                               // Toast.makeText(AddToGroup.this, "Inserted", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddToGroup.this, "Fail", Toast.LENGTH_SHORT).show();

                            }
                        });

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