package com.example.finalyearproject117477692;
import org.parceler.Parcel;

// class used for members
@Parcel
public class Member {
    private String Name;
    private Integer Age;
    private String Contact;
    private String key;



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

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
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
        return this.Name + ", " + Age + " years of age - " + Contact;
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

