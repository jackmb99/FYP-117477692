package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

// code adapted from michael gleeson android lectures
public class ShowGoals extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private DatabaseReference reff;

    private FloatingActionButton fab;
    private ListView listView;
    private GoalListViewAdapter listViewAdapter;
    private List<Goal> listGoal = new ArrayList<>();

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goals);
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
                        return true;
                }
                return false;
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("IndividualGoals");
        // reff = FirebaseDatabase.getInstance().getReference("CompletedGoals");

        initUI();
        setListViewAdapter();

        addSingleEventListener();
        addChildEventListener();


        setFabClickListener();
        setListViewItemListener();
        setListViewLongClickListener();
    }
    private void initUI(){
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);
    }

    private void setListViewAdapter(){
        listViewAdapter = new GoalListViewAdapter(this, listGoal);
        listView.setAdapter(listViewAdapter);
    }

    private void addChildEventListener() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Goal goal = dataSnapshot.getValue(Goal.class);
                if(goal != null){
                    goal.setKey(dataSnapshot.getKey());
                    listGoal.add(dataSnapshot.getValue(Goal.class));
                    listViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Goal goal = dataSnapshot.getValue(Goal.class);
                if(goal != null){
                    String key = dataSnapshot.getKey();
                    for(int i=0;i<listGoal.size();i++){
                        Goal goal1 = listGoal.get(i);
                        if(goal1.getKey().equals(key)){
                            listGoal.set(i, goal);
                            listViewAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listGoal.remove(dataSnapshot.getValue(Goal.class));
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

    private void setListViewItemListener(){
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("edit", true);
            bundle.putParcelable("goal", Parcels.wrap(listGoal.get(i)));
            Intent intent = new Intent(this, EditingGoal.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setListViewLongClickListener(){
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Goal goal = listGoal.get(i);

            new AlertDialog.Builder(this)
                    .setTitle("Delete " + goal.getTitle() + ", " + goal.getDescription())
                    .setMessage("Do you want to delete the selected record?")
                    .setPositiveButton("Delete", (dialogInterface, i1) -> {
                        databaseReference.child(goal.getKey()).removeValue();
                       /* databaseReference = FirebaseDatabase.getInstance().getReference("CompletedGoals");
                        String key = databaseReference.push().getKey();
                        goal.setKey(key);
                        databaseReference.child(key).setValue(goal);*/

                        Toast.makeText(ShowGoals.this, "Goal removed successfully", Toast.LENGTH_SHORT).show();

                    })
                    .setNegativeButton("Cancel", (dialogInterface, i12) -> {
                        dialogInterface.dismiss();
                    })
                    .create()
                    .show();
            return true;
        });
    }


    private void setFabClickListener() {
        fab.setOnClickListener(e -> {
            startActivity(new Intent(this, EditGoalActivity.class));
        });
    }
}