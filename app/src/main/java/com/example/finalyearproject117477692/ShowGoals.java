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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

// code adapted from michael gleeson android lectures
public class ShowGoals extends AppCompatActivity {
    private DatabaseReference databaseReference;
   // private DatabaseReference reff;

    // declaring variables
    private FloatingActionButton fab;
    private Query query;
    private ListView listView;
    private GoalListViewAdapter listViewAdapter;
    private List<Goal> listGoal = new ArrayList<>();
    ActionBar actionBar;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_goals);

        // action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Show Goals");

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
                        return true;
                }
                return false;
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("IndividualGoals");
        // reff = FirebaseDatabase.getInstance().getReference("CompletedGoals");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        query = FirebaseDatabase.getInstance().getReference("IndividualGoals")
                .orderByChild("userUid")
                .equalTo(uid);

        initUI();
        setListViewAdapter();

        addSingleEventListener();
        addChildEventListener();


        setFabClickListener();
        setListViewItemListener();
        setListViewLongClickListener();
    }
    // initialising interface
    private void initUI(){
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);
    }

    private void setListViewAdapter(){
        listViewAdapter = new GoalListViewAdapter(this, listGoal);
        listView.setAdapter(listViewAdapter);
    }
// show goals
    private void addChildEventListener() {
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Goal goal = dataSnapshot.getValue(Goal.class);
                if(goal != null){
                    goal.setKey(dataSnapshot.getKey());
                    listGoal.add(dataSnapshot.getValue(Goal.class));
                    listViewAdapter.notifyDataSetChanged();
                }
            }
// what happens if goals are updated
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
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    // wrapping selected item in a bundle and passing it to activity in order to update
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

    //delete goal
    private void setListViewLongClickListener(){
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            // getting the specific goal that was clicked from list
            Goal goal = listGoal.get(i);
            new AlertDialog.Builder(this)
                    .setTitle("Complete " + goal.getTitle() + ", " + goal.getDescription())
                    .setMessage("Do you want to complete the selected record?")
                    .setPositiveButton("Complete", (dialogInterface, i1) -> {
                        // Removing data from database
                        databaseReference.child(goal.getKey()).removeValue();

                        Toast.makeText(ShowGoals.this, "Goal completed, well done", Toast.LENGTH_SHORT).show();

                    })
                    .setNegativeButton("Cancel", (dialogInterface, i12) -> {
                        dialogInterface.dismiss();
                    })
                    .create()
                    .show();
            return true;
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
                Toast.makeText(ShowGoals.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
            case R.id.menuHome:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return true;
    }


    private void setFabClickListener() {
        fab.setOnClickListener(e -> {
            startActivity(new Intent(this, EditGoalActivity.class));
        });
    }
}
/* databaseReference = FirebaseDatabase.getInstance().getReference("CompletedGoals");
                        String key = databaseReference.push().getKey();
                        goal.setKey(key);
                        databaseReference.child(key).setValue(goal);*/