package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //declaring images and making them clickable in order to go to next activity
        ImageView myImage = (ImageView)findViewById(R.id.myImage);
        ImageView myImage2 = (ImageView)findViewById(R.id.myImage2);
        ImageView myImage3 = (ImageView)findViewById(R.id.myImage3);
        ImageView myImage4 = (ImageView)findViewById(R.id.myImage4);
        ImageView myImage5 = (ImageView)findViewById(R.id.myImage5);
        ImageView myImage6 = (ImageView)findViewById(R.id.myImage6);

        // convert
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        //  group members
        myImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowMembers.class);
                startActivity(intent);
            }
        });
        // goals
        myImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowGoals.class);
                startActivity(intent);
            }
        });
        // covid api
        myImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Covid.class);
                startActivity(intent);
            }
        });
        // chart
        myImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Chart.class);
                startActivity(intent);
            }
        });
        // leaderboard
        myImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
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
