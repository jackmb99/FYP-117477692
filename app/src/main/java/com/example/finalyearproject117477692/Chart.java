package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// adapted from https://www.youtube.com/watch?v=C0O9u0jd6nQ
// Video is on line-charts, I changed to bar-chart with trial and error
// Added my own increment of x axis

public class Chart extends AppCompatActivity {

    // declare variables
    EditText yValue;
    Button insertBtn;
    BarChart barChart;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    public static String uid;
    private ProgressBar progressBar;
    ActionBar actionBar;


    ArrayList<IBarDataSet> iBarDataSets = new ArrayList<>();
    BarData barData;
    int count; // used to increment x axis

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        actionBar = getSupportActionBar();
        actionBar.setTitle("5K Progression");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        progressBar = findViewById(R.id.progressBar);

        yValue = findViewById(R.id.yTextView);
        insertBtn = findViewById(R.id.btnInsert);
        barChart = findViewById(R.id.lineChartView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("ChartValues").child(uid);
        insertData();
        /*lineDataSet.setLineWidth(4);*/
    }

    private void insertData(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 count = (int) snapshot.getChildrenCount();// total number of database chart entries
                 retrieveData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(yValue.getText().toString().isEmpty()) {
                    yValue.setError("Error");
                }else{
                    String id = myRef.push().getKey();
                    int y = Integer.parseInt(yValue.getText().toString());
                    // write to db & increment x axis
                    int x = count;
                    DataPoint dataPoint = new DataPoint(x,y);
                    myRef.child(id).setValue(dataPoint);

                    yValue.setText("");

                    retrieveData();
                }

            }
        });
    }

    // retrieve chart data
    private void retrieveData(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<BarEntry> dataVals = new ArrayList<BarEntry>();

                if(snapshot.hasChildren()){
                    for(DataSnapshot myDataSnapshot : snapshot.getChildren()){
                        DataPoint dataPoint = myDataSnapshot.getValue(DataPoint.class);
                        dataVals.add(new BarEntry(dataPoint.getxValue(), dataPoint.getyValue()));
                    }

                    showChart(dataVals);

                }else{
                    progressBar.setVisibility(View.GONE);
                    barChart.clear();
                    barChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //display chart
    private void showChart(ArrayList<BarEntry> dataVals){
        BarDataSet barDataSet = new BarDataSet(dataVals, null);
        barDataSet.setValueTextSize(12);
        barDataSet.setValues(dataVals);
        barDataSet.setLabel("5km Progress");
        iBarDataSets.clear();
        iBarDataSets.add(barDataSet);
        barData = new BarData(iBarDataSets);
        barChart.clear();
        barChart.setData(barData);
        barChart.invalidate();
        progressBar.setVisibility(View.GONE);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(Chart.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
    }
}