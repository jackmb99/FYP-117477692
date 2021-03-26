package com.example.finalyearproject117477692;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;
    public static String uid;
    public static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Main Dashboard");

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // check if user in group
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


        //declaring images and making them clickable in order to go to next activity
        ImageView myImage = (ImageView)findViewById(R.id.myImage);
        ImageView myImage2 = (ImageView)findViewById(R.id.myImage2);
        ImageView myImage3 = (ImageView)findViewById(R.id.myImage3);
        ImageView myImage4 = (ImageView)findViewById(R.id.myImage4);
        ImageView myImage5 = (ImageView)findViewById(R.id.myImage5);
        ImageView myImage6 = (ImageView)findViewById(R.id.myImage6);
        ImageView myImage7 = (ImageView)findViewById(R.id.myImage7);
        ImageView myImage8 = (ImageView)findViewById(R.id.myImage8);
        ImageView myImage9 = (ImageView)findViewById(R.id.myImage9);
        ImageView myImage10 = (ImageView)findViewById(R.id.myImage10);

        // goals
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowGoals.class);
                startActivity(intent);
            }
        });
        //  individual
        myImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowMembers.class);
                startActivity(intent);
            }
        });
        // group
        myImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Group.class);
                startActivity(intent);
            }
        });
        // group leaderboard
        myImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.equals(uid)) {
                    System.out.println(user + " and " + uid);
                    Intent intent = new Intent(MainActivity.this, GroupLeaderboardActivity.class);
                    startActivity(intent);
                } else{
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Group Error");
                    alertDialog.setMessage("You must be a member of group to view Group Leaderboard");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }
        });
        // leaderboard
        myImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });
        // chart
        myImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Chart.class);
                startActivity(intent);
            }
        });
        // covid
        myImage7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Covid.class);
                startActivity(intent);
            }
        });
        // maps
        myImage8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        // converter
        myImage9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        // timer
        myImage10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Timer.class);
                startActivity(intent);
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
                // https://www.youtube.com/watch?v=FmZLWe_gaSY Log out functionality
                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
    }
}
