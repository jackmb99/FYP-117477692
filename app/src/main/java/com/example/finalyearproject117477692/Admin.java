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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class Admin extends AppCompatActivity {
    private Query query;
    public static long distanceVar;
    public static String uid;
    DistanceData distance;
    public static boolean alreadyExecuted = false;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Admin");


        Button btnUpdate = findViewById(R.id.btnUpdate);
        EditText etEmailAddress = findViewById(R.id.etEmailAddress);
        EditText etSubtracted = findViewById(R.id.etSubtracted);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("Group");


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alreadyExecuted = false;
                String name = etEmailAddress.getText().toString();
                System.out.println(name);
                String subtractedDistance = String.valueOf(etSubtracted.getText());
                double subtractedDistanceFinal = Double.parseDouble(subtractedDistance);
                distance = new DistanceData();

                query = FirebaseDatabase.getInstance().getReference("Group")
                        .orderByChild("name")
                        .equalTo(name);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot distanceSnapshot : snapshot.getChildren()){
                            distance = distanceSnapshot.getValue(DistanceData.class);
                            distanceVar = distance.getDistance();
                            uid = distance.getUserUid();
                            System.out.println(uid);
                            System.out.println(distanceVar);

                        }

                        if(!alreadyExecuted) {
                            alreadyExecuted = true;
                            insert();

                        }
                    }

                    private void insert() {
                        double finalDistance = distanceVar - subtractedDistanceFinal;

                        HashMap map = new HashMap();
                        map.put("name", name);
                        map.put("distance", finalDistance);
                        map.put("userUid", uid);

                        FirebaseDatabase.getInstance().getReference("Group").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Admin.this, "Inserted", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Admin.this, "Fail", Toast.LENGTH_SHORT).show();

                            }
                        });
                        etEmailAddress.setText("");
                        etSubtracted.setText("");
                        etEmailAddress.requestFocus();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });




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
                finish();
                Toast.makeText(Admin.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
    }
}