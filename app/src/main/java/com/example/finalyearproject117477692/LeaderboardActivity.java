package com.example.finalyearproject117477692;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// leaderboard functionality from https://www.youtube.com/watch?v=j1_tEaYchyk

public class LeaderboardActivity extends AppCompatActivity {

    // declaring variables
    private static final String TAG = "****Log check****";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<DistanceData> list;
    DistanceAdapter adapter;
    DatabaseReference reference;
    TextView name,distance,rank;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        //initialising variables
        recyclerView = findViewById(R.id.leaderboard_recycler);
        progressBar = findViewById(R.id.leaderboardProgress);

        // action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Leaderboard");

        reference = FirebaseDatabase.getInstance().getReference().child("Distance");
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        name = findViewById(R.id.tvUsername);
        distance = findViewById(R.id.tvDistance);
        rank = findViewById(R.id.tvRank);



        // ordering from highest to lowest based on distance travelled
        reference.orderByChild("distance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DistanceData data = dataSnapshot.getValue(DistanceData.class);
                    list.add(data);
                }

                adapter = new DistanceAdapter(list, LeaderboardActivity.this);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LeaderboardActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}