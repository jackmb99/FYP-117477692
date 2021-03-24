package com.example.finalyearproject117477692;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class GroupExercise extends AppCompatActivity {

    public static String distance;
    public static String distanceGroup;
    private boolean edit = false;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_exercise);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Log Group Activity");

        Button btnExercise = findViewById(R.id.btnExercise);
        EditText etExercise = findViewById(R.id.etExercise);
        EditText etDistanceExercised = findViewById(R.id.etDistanceExercised);
        EditText etComment = findViewById(R.id.etComment);
        Exercise exercise = new Exercise();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("Distance");
        DatabaseReference dataRef2 = database.getReference("GroupExercise");
        DatabaseReference dataRef3 = database.getReference("Group");

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // here you can get your data from this snapshot object
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    /*String data = dataSnapshot.getValue("distance").toString();*/
                    distance = dataSnapshot.child(uid).child("distance").getValue().toString();
                    System.out.println(distance);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        dataRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // here you can get your data from this snapshot object
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    /*String data = dataSnapshot.getValue("distance").toString();*/
                    distanceGroup = dataSnapshot.child(uid).child("distance").getValue().toString();
                    System.out.println(distanceGroup);
                }
            }
                @Override
                public void onCancelled (DatabaseError databaseError){

                }

        });

        btnExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                System.out.println(distance);
                System.out.println(distanceGroup);


                double distanceTravelled = Double.parseDouble(distance);
                double distanceTravelledGroup = Double.parseDouble(distanceGroup);
                if(etExercise.getText().toString().isEmpty()){
                    etExercise.setError("Error");
                }else if(etDistanceExercised.getText().toString().isEmpty()) {
                    etDistanceExercised.setError("Error");
                }else if(etComment.getText().toString().isEmpty()){
                    etComment.setError("Error");
                }
                else {

                String dbDistance = String.valueOf(etDistanceExercised.getText());
                double semiDistance = Double.parseDouble(dbDistance);

                double finalDistance = semiDistance + distanceTravelled;
                double finalGroupDistance = semiDistance + distanceTravelledGroup;


                    HashMap map = new HashMap();
                    map.put("name", email);
                    map.put("distance", finalDistance);
                    map.put("userUid", uid);

                    FirebaseDatabase.getInstance().getReference("Distance").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(GroupExercise.this, "Inserted", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GroupExercise.this, "Fail", Toast.LENGTH_SHORT).show();

                        }
                    });
                    HashMap map2 = new HashMap();
                    map2.put("name", email);
                    map2.put("userUid", uid);
                    map2.put("distance", finalGroupDistance);

                    FirebaseDatabase.getInstance().getReference("Group").child(uid).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(GroupExercise.this, "Inserted", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GroupExercise.this, "Fail", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                if(etExercise.getText().toString().isEmpty()){
                    etExercise.setError("Error");
                }else if(etDistanceExercised.getText().toString().isEmpty()) {
                    etDistanceExercised.setError("Error");
                }else if(etComment.getText().toString().isEmpty()){
                    etComment.setError("Error");
                }
                else{
                    String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                    // int mdistance = Integer.parseInt(etDistanceExercised.getText().toString().trim());
                    exercise.setExerciseType(etExercise.getText().toString().trim());
                    exercise.setDistanceCovered(etDistanceExercised.getText().toString().trim());
                    exercise.setComment(etComment.getText().toString().trim());
                    exercise.setEmail(email);
                    exercise.setDate(timeStamp);

                    if(edit){
                        dataRef2.child(exercise.getKey()).setValue(exercise);
                    }else{
                        // writing to database
                        String key = dataRef2.push().getKey();
                        exercise.setKey(key);
                        dataRef2.child(key).setValue(exercise);
                    }
                    /*finish();*/
                    Intent intent = new Intent(GroupExercise.this, ShowGroupExercise.class);
                    startActivity(intent);
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

    // log out
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(GroupExercise.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
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