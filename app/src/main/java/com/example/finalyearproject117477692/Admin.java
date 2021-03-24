package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class Admin extends AppCompatActivity {
    private Query query;
    private Query query2;
    public static long distanceVar;
    public static long solodistanceVar;
    public static String uid;
    public static String uid2;
    public static String uid3;
    DistanceData distance;
    DistanceData distance2;
    public static boolean alreadyExecuted = false;
    public static boolean alreadyExecuted2 = false;
    ActionBar actionBar;
    Spinner spinner;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDataList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Admin");


        spinner = findViewById(R.id.mySpinner);
        Button btnUpdate = findViewById(R.id.btnUpdate);
       // EditText etEmailAddress = findViewById(R.id.etEmailAddress);
        EditText etSubtracted = findViewById(R.id.etSubtracted);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = database.getReference("Group");
        databaseReference = database.getReference("Users");
        spinnerDataList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(Admin.this,R.layout.support_simple_spinner_dropdown_item,spinnerDataList);
        spinner.setAdapter(adapter);
        retrieveData();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spinner.getSelectedItem().toString().equals("Please select a user")){
                    Toast.makeText(Admin.this, "Please select a user", Toast.LENGTH_SHORT).show();
                } else if (etSubtracted.getText().toString().isEmpty()) {
                    etSubtracted.setError("Error");
                } else {
                    alreadyExecuted = false;
                    alreadyExecuted2 = false;
                    String name = spinner.getSelectedItem().toString();
                    System.out.println(name);
                    String subtractedDistance = String.valueOf(etSubtracted.getText());
                    double subtractedDistanceFinal = Double.parseDouble(subtractedDistance);
                    distance = new DistanceData();

                    query = FirebaseDatabase.getInstance().getReference("Group")
                            .orderByChild("name")
                            .equalTo(name);
                    query2 = FirebaseDatabase.getInstance().getReference("Distance")
                            .orderByChild("name")
                            .equalTo(name);

                    /*query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                distance2 = snapshot1.getValue(DistanceData.class);
                                uid2 = distance2.getName();
                                uid3 = distance2.getUserUid();
                                System.out.println(uid2 + "query2");
                                System.out.println(uid3 + "heyyy");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/


                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot distanceSnapshot : snapshot.getChildren()) {
                                distance = distanceSnapshot.getValue(DistanceData.class);
                                distanceVar = distance.getDistance();
                                uid = distance.getUserUid();
                                uid2 = distance.getName();
                                System.out.println(uid);
                                System.out.println(distanceVar);
                            }
                            if (!alreadyExecuted) {
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

                            if (spinner.getSelectedItem().toString().equals(uid2)) {


                                FirebaseDatabase.getInstance().getReference("Group").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Admin.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Admin.this, "Fail", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                spinner.setSelection(0);
                                etSubtracted.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot distanceSnapshot : snapshot.getChildren()) {
                                System.out.println("query2ready");
                                distance = distanceSnapshot.getValue(DistanceData.class);
                                solodistanceVar = distance.getDistance();
                                uid3 = distance.getUserUid();
                                System.out.println(uid3 + "thisuidwanted");
                                System.out.println(solodistanceVar);
                            }
                            if (!alreadyExecuted2) {
                                alreadyExecuted2 = true;
                                insert2();
                            }
                        }

                        private void insert2() {
                            System.out.println("insert2on");
                            double finalDistance = solodistanceVar - subtractedDistanceFinal;

                            HashMap map2 = new HashMap();
                            map2.put("name", name);
                            map2.put("distance", finalDistance);
                            map2.put("userUid", uid3);

                            FirebaseDatabase.getInstance().getReference("Distance").child(uid3).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Admin.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Admin.this, "Fail", Toast.LENGTH_SHORT).show();

                                }
                            });
                            spinner.setSelection(0);
                            etSubtracted.setText("");
                         //   etEmailAddress.requestFocus();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }
            }
        });
    }

    public void retrieveData(){
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()){
                    spinnerDataList.add(item.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                Toast.makeText(Admin.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
    }
}