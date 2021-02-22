package com.example.finalyearproject117477692;
import org.parceler.Parcel;

// class for goals
@Parcel
public class Goal {
    private String Title;
    private String Description;
    private String key;

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
