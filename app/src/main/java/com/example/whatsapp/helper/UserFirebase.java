package com.example.whatsapp.helper;

import com.example.whatsapp.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class UserFirebase {

    public static String getIdUser(){
        FirebaseAuth user = ConfigFirebase.getFirebaseAuthentication();
        String email = user.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(email);
        return idUser;
    }
}
