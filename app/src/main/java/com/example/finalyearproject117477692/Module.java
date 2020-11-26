package com.example.finalyearproject117477692;

import android.app.Application;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Module extends Application {
    public ArrayList <String> garrList = new ArrayList<>();
    public ArrayAdapter<String> garrAdp;
    public String gvalue_id;
    public String gvalue_Name;



    public String getGvalue_id() {
        return gvalue_id;
    }

    public void setGvalue_id(String gvalue_id) {
        this.gvalue_id = gvalue_id;
    }

    public String getGvalue_Name() {
        return gvalue_Name;
    }

    public void setGvalue_Name(String gvalue_Name) {
        this.gvalue_Name = gvalue_Name;
    }
}
