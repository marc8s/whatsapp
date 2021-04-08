package com.example.whatsapp.model;

import com.example.whatsapp.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class User {
    private String name, password, email, id;

    public User() {
    }

    public void save(){
        DatabaseReference referenceFirebase = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference user = referenceFirebase.child("user").child(getId());
        user.setValue(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
