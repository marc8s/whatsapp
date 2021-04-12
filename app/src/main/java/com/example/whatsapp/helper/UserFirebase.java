package com.example.whatsapp.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfigFirebase;
import com.example.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserFirebase {

    public static String getIdUser(){
        FirebaseAuth user = ConfigFirebase.getFirebaseAuthentication();
        String email = user.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(email);
        return idUser;
    }

    public static FirebaseUser getUser(){
        FirebaseAuth user = ConfigFirebase.getFirebaseAuthentication();
        return user.getCurrentUser();
    }

    public static boolean updateNameUser(String name){
        try {
            FirebaseUser user = getUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Profile", "Erro ao atualizar nome de perfil");

                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public static boolean updatePicUser(Uri url){
        try {
            FirebaseUser user = getUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Profile", "Erro ao atualizar foto de perfil");

                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


    }

    public static User getDataUser(){
        FirebaseUser firebaseUser = getUser();
        User user = new User();
        user.setEmail(firebaseUser.getEmail());
        user.setName(firebaseUser.getDisplayName());

        if(firebaseUser.getPhotoUrl() == null){
            user.setProfilePic("");
        }else {
            user.setProfilePic(firebaseUser.getPhotoUrl().toString());
        }

        return user;
    }
}
