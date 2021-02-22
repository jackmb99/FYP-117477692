package com.example.finalyearproject117477692;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// used for testing
public class Testing extends AppCompatActivity {

    EditText etDistanceTravelled;
    Button btnSaved;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String DISTANCE = "distance";
    Distance distanceTrav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        etDistanceTravelled = findViewById(R.id.etDistanceTravelled);
        btnSaved = findViewById(R.id.btnSaved);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("distance");
        mAuth = FirebaseAuth.getInstance();


        btnSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distanceTravelled = String.valueOf(etDistanceTravelled.getText());

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                distanceTrav = new Distance(distanceTravelled);

                mDatabase = FirebaseDatabase.getInstance().getReference("Distance").child(uid);

                mDatabase.setValue(distanceTrav);


            }
        });

    }


}