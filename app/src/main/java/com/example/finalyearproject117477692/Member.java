package com.example.finalyearproject117477692;

public class Member {
    private String Name;
    private Integer Age;
    private String Contact;

    public Member() {
    }

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

    public String toString(){
        return this.Name + ", " + Age + " years of age - " + Contact;
    }
}
