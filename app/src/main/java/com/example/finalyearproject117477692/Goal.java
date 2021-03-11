package com.example.finalyearproject117477692;
import org.parceler.Parcel;

// class for goals
@Parcel
public class Goal {
    private String Title;
    private String Description;
    private String key;
    private String userUid;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    //constructor
    public Goal() {
    }

    // Code from Michael Gleesons CRUD on firebase
    @Override
    public boolean equals(Object object){
        if(object == null)
            return false;
        if(!Goal.class.isAssignableFrom(object.getClass()))
            return false;
        final Goal goal = (Goal)object;
        return goal.getKey().equals(key);
    }

}
