package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Group extends AppCompatActivity {

    // declare variables
    public static String user;
    public static String uid;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Group");

        Button btnGroup = findViewById(R.id.btnGroup);
        Button btnGoto = findViewById(R.id.btnGoto);
        Button btnPlanner = findViewById(R.id.btnPlanner);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference dataRef = database.getReference("Group");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // here you can get your data from this snapshot object
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    System.out.println(uid);


                    /*String data = dataSnapshot.getValue("distance").toString();*/
                    if (dataSnapshot.child(uid).child("userUid").getValue() == null) {
                        user = "invalidnametesttttt";
                    } else {
                        user = dataSnapshot.child(uid).child("userUid").getValue().toString();
                    }
                    System.out.println(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        // join group if user not already in group
        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.equals(uid)) {
                    System.out.println(user + " and " + uid);
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    HashMap map = new HashMap();
                    map.put("name", email);
                    map.put("userUid", uid);
                    map.put("distance", 0);

                    FirebaseDatabase.getInstance().getReference("Group").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(Group.this, "Inserted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Group.this, ShowGroupExercise.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Group.this, "Fail", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    Toast.makeText(Group.this, "User already in group", Toast.LENGTH_SHORT).show();
                }


            }
        });

        // go to group if user is a member
        btnGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.equals(uid)) {
                    System.out.println(user + " and " + uid);
                    Intent intent = new Intent(Group.this, ShowGroupExercise.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(Group.this, "User not a member of group", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // go to planner if user is a group member
        btnPlanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.equals(uid)) {
                    System.out.println(user + " and " + uid);
                    Intent intent = new Intent(Group.this, Planner.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(Group.this, "User not a member of group", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    // overide back button functionality
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Group.this, MainActivity.class));
        finish();

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
                Toast.makeText(Group.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
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