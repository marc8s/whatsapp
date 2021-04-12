package com.example.whatsapp.model;

import com.example.whatsapp.config.ConfigFirebase;
import com.example.whatsapp.helper.UserFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name, password, email, id, profilePic;

    public User() {
    }

    public void save(){
        DatabaseReference referenceFirebase = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference user = referenceFirebase.child("user").child(getId());
        user.setValue(this);
    }

    public void update(){
        String idUser = UserFirebase.getIdUser();
        DatabaseReference referenceFirebase = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference user = referenceFirebase.child("user")
                .child(idUser);

        Map<String, Object> valuesUser = convertToMap();

        user.updateChildren(valuesUser);
    }
    //necessário para atualizar em configurações
    @Exclude
    public Map<String, Object> convertToMap(){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", getEmail());
        userMap.put("name", getName());
        userMap.put("profilePic", getProfilePic());

        return userMap;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
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
