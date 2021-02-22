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
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

// https://www.youtube.com/watch?v=x7e1YCQVGME - reference for showing data from firebase in list
public class ShowMembers extends AppCompatActivity {

    //declaring variables
    private DatabaseReference databaseReference;

    private FloatingActionButton fab;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<Member> listPerson = new ArrayList<>();

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_members);

        //initialise and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

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

                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), ShowGoals.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("GroupMembers");

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
        listViewAdapter = new ListViewAdapter(this, listPerson);
        listView.setAdapter(listViewAdapter);
    }

    // show members
    private void addChildEventListener() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Member member = dataSnapshot.getValue(Member.class);
                if(member != null){
                    member.setKey(dataSnapshot.getKey());
                    listPerson.add(dataSnapshot.getValue(Member.class));
                    listViewAdapter.notifyDataSetChanged();
                }
            }

            // what happens on change
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Member member = dataSnapshot.getValue(Member.class);
                if(member != null){
                    String key = dataSnapshot.getKey();
                    for(int i=0;i<listPerson.size();i++){
                        Member person1 = listPerson.get(i);
                        if(person1.getKey().equals(key)){
                            listPerson.set(i, member);
                            listViewAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listPerson.remove(dataSnapshot.getValue(Member.class));
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

    // edit member
    private void setListViewItemListener(){
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("edit", true);
            bundle.putParcelable("member", Parcels.wrap(listPerson.get(i)));
            Intent intent = new Intent(this, EditPersonActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    // delete member
    private void setListViewLongClickListener(){
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            Member member = listPerson.get(i);
            new AlertDialog.Builder(this)
                    .setTitle("Delete " + member.getName() + ", " + member.getContact())
                    .setMessage("Do you want to delete the selected record?")
                    .setPositiveButton("Delete", (dialogInterface, i1) -> {
                        databaseReference.child(member.getKey()).removeValue();
                        Toast.makeText(ShowMembers.this, "Member removed successfully", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i12) -> {
                        dialogInterface.dismiss();
                    })
                    .create()
                    .show();
            return true;
        });
    }

    // create new
    private void setFabClickListener() {
        fab.setOnClickListener(e -> {
            startActivity(new Intent(this, AddToGroup.class));
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
                Toast.makeText(ShowMembers.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LogInActivity.class));
                break;
        }
        return true;
    }
}