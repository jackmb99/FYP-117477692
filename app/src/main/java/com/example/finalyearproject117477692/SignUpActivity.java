package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


// Sign up functionality adapted from -> https://www.youtube.com/watch?v=mF5MWLsb4cg

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    // declaring variables
    EditText etEmail, etPassword;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressbar);

        findViewById(R.id.btnSignup).setOnClickListener(this);
        findViewById(R.id.tvLogIn).setOnClickListener(this);

        reference = FirebaseDatabase.getInstance().getReference().child("Distance");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }
// validation
    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            etPassword.setError("Minimum length of password is 6 characters");
            etPassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        // sign up
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("name", email); // idea from https://www.youtube.com/watch?v=j1_tEaYchyk
                    map.put("distance", 0);
                    /*reference.child(currentUser.getUid()).setValue(map);*/
                    FirebaseDatabase.getInstance().getReference("Distance").child(uid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SignUpActivity.this, "Inserted", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Fail", Toast.LENGTH_SHORT).show();

                        }
                    });
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else{

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSignup:
                registerUser();
                break;

            case R.id.tvLogIn:
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
    }
}