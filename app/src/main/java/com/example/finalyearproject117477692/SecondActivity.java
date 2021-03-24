package com.example.finalyearproject117477692;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

// idea for code came from Michael Gleeson CA1 exam, code is my own

public class SecondActivity extends AppCompatActivity {

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // action bar title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Distance Converter");

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // bottomNavigationView.setSelectedItemId(R.id.home);

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
                        startActivity(new Intent(getApplicationContext(), ShowGoals.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        //declaring variables
        final EditText etEnterAmount = findViewById(R.id.etEnterAmount);
        final Spinner spDistanceType = findViewById(R.id.spDistanceType);
        Button btnConvert = findViewById(R.id.btnConvert);
        final TextView tvResult = findViewById(R.id.tvResult);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etEnteredDistanceType = String.valueOf(etEnterAmount.getText());
                double convertedValue, finalValue;

                if(etEnteredDistanceType.isEmpty()){
                    etEnterAmount.setError("Error: Please enter a distance amount"); // setting an error message if text is empty
                  //  Toast.makeText(SecondActivity.this, "Please enter a distance amount", Toast.LENGTH_SHORT).show();
                }else{
                    final double distanceValue = Double.parseDouble(String.valueOf(etEnterAmount.getText()));
                    switch (spDistanceType.getSelectedItemPosition()){
                        case 0:
                        //error if spinner empty
                            Toast.makeText(SecondActivity.this, "Please select a distance type", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            convertedValue = convertToKm(distanceValue); //brings you to convert to kilometres function
                            finalValue = Math.round(convertedValue * 100.0)/100.0;
                            AlertDialog alertDialog = new AlertDialog.Builder(SecondActivity.this).create();
                            alertDialog.setTitle("Conversion");
                            alertDialog.setMessage(etEnteredDistanceType + " Miles is\n" + String.valueOf(finalValue) + " Kilometres");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            etEnterAmount.setText("");
                           // tvResult.setText(etEnteredDistanceType + " Miles is\n" + String.valueOf(finalValue) + " Kilometres");
                           // tvResult.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            convertedValue = convertToMiles(distanceValue); //brings you to convert to miles function
                            finalValue = Math.round(convertedValue * 100.0)/100.0;
                            AlertDialog alertDialog2 = new AlertDialog.Builder(SecondActivity.this).create();
                            alertDialog2.setTitle("Conversion");
                            alertDialog2.setMessage(etEnteredDistanceType + " Miles is\n" + String.valueOf(finalValue) + " Kilometres");
                            alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog2.show();
                            etEnterAmount.setText("");
                          //  tvResult.setText(etEnteredDistanceType + " Kilometres is\n" + String.valueOf(finalValue) + " Miles");
                            // tvResult.setVisibility(View.VISIBLE);
                            break;
                    }

                }
            }
        });
    }
    //functions to convert to miles or kms
    public double convertToKm(double x){
        double resultValue;
        resultValue = (x * 1.60934);
        return resultValue;
    }
    public double convertToMiles(double y){
        double resultValue;
        resultValue = (y * 0.621371);
        return resultValue;
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
                Toast.makeText(SecondActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
    }
}