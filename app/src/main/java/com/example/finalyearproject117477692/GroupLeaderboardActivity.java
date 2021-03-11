package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// leaderboard functionality from https://www.youtube.com/watch?v=j1_tEaYchyk

public class GroupLeaderboardActivity extends AppCompatActivity {

    // declaring variables
    private static final String TAG = "****Log check****";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<DistanceData> list;
    GroupDistanceAdapter adapter;
    DatabaseReference reference;
    TextView name,distance,rank;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_leaderboard);

        //initialising variables
        recyclerView = findViewById(R.id.groupleaderboard_recycler);
        progressBar = findViewById(R.id.groupleaderboardProgress);

        // action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Group Leaderboard");


        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        name = findViewById(R.id.tvUsername2);
        distance = findViewById(R.id.tvDistance2);
        rank = findViewById(R.id.tvRank2);


        reference = FirebaseDatabase.getInstance().getReference().child("Group");
        // ordering from highest to lowest based on distance travelled in group
        reference.orderByChild("distance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DistanceData data = dataSnapshot.getValue(DistanceData.class);
                    list.add(data);
                }

                adapter = new GroupDistanceAdapter(list, GroupLeaderboardActivity.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(GroupLeaderboardActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(GroupLeaderboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
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