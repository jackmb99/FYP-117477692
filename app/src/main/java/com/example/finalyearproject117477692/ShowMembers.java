package com.example.finalyearproject117477692;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        databaseReference = FirebaseDatabase.getInstance().getReference("GroupMembers");

        initUI();
        setListViewAdapter();

        addSingleEventListener();
        addChildEventListener();

        setFabClickListener();
        setListViewItemListener();
        setListViewLongClickListener();
    }

    private void initUI(){
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);
    }

    private void setListViewAdapter(){
        listViewAdapter = new ListViewAdapter(this, listPerson);
        listView.setAdapter(listViewAdapter);
    }

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

    private void setFabClickListener() {
        fab.setOnClickListener(e -> {
            startActivity(new Intent(this, AddToGroup.class));
        });
    }
}