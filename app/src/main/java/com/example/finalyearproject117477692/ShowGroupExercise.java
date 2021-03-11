package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowGroupExercise extends AppCompatActivity {

    private DatabaseReference databaseReference;
    // private DatabaseReference reff;

    // declaring variables
    private FloatingActionButton fab;
    private ListView listView;
    private ExerciseListViewAdapter listViewAdapter;
    private List<Exercise> listExercise = new ArrayList<>();

    private ProgressBar progressBar;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group_exercise);

        actionBar = getSupportActionBar();
        actionBar.setTitle("View Group Exercises");


        databaseReference = FirebaseDatabase.getInstance().getReference("GroupExercise");


        initUI();
        setListViewAdapter();
        setFabClickListener();

        addSingleEventListener();
        addChildEventListener();


    }
    // initialising interface
    private void initUI(){
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);
    }

    private void setListViewAdapter(){
        listViewAdapter = new ExerciseListViewAdapter(this, listExercise);
        listView.setAdapter(listViewAdapter);
    }

    // show members
    private void addChildEventListener() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Exercise exercise = dataSnapshot.getValue(Exercise.class);
                if(exercise != null){
                    exercise.setKey(dataSnapshot.getKey());
                    listExercise.add(dataSnapshot.getValue(Exercise.class));
                    listViewAdapter.notifyDataSetChanged();
                }
            }

            // what happens on change
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Exercise exercise = dataSnapshot.getValue(Exercise.class);
                if(exercise != null){
                    String key = dataSnapshot.getKey();
                    for(int i=0;i<listExercise.size();i++){
                        Exercise exercise1 = listExercise.get(i);
                        if(exercise1.getKey().equals(key)){
                            listExercise.set(i, exercise);
                            listViewAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listExercise.remove(dataSnapshot.getValue(Exercise.class));
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void addSingleEventListener(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    // create new exercise
    private void setFabClickListener() {
        fab.setOnClickListener(e -> {
            startActivity(new Intent(this, GroupExercise.class));
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
                Toast.makeText(ShowGroupExercise.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
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