package com.example.finalyearproject117477692;
import org.parceler.Parcel;

// class used for members
@Parcel
public class Member {
    private String Name;
    private int Contact;
    private String key;
    private String userUid;
    private String dateStamp;

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }



    //constructor
    public Member() {
    }

    //getters and setters for member class
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }


    public int getContact() {
        return Contact;
    }

    public void setContact(int contact) {
        Contact = contact;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //format that I want the members of database to print out as
    public String toString(){
        return this.Name + ", " + " years of age - " + Contact;
    }


    // code from Michael Gleesons lecture
    @Override
    public boolean equals(Object object){
        if(object == null)
            return false;
        if(!Member.class.isAssignableFrom(object.getClass()))
            return false;
        final Member member = (Member)object;
        return member.getKey().equals(key);
    }

}

