package com.example.whatsapp.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {
    private static FirebaseAuth mAuthentication;
    private static DatabaseReference mReferenceFirebase;
    private static StorageReference mStorage;

    //RETORNA UMA INSTANCIA DO FIREBASEDATABASE
    public static DatabaseReference getFirebaseDatabase(){
        if(mReferenceFirebase == null){
            mReferenceFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return mReferenceFirebase;
    }
    //RETORNA UMA INSTANCIA DO FIREBASEAUTH
    public static FirebaseAuth getFirebaseAuthentication() {
        if(mAuthentication == null){
            mAuthentication = FirebaseAuth.getInstance();
        }
        return mAuthentication;
    }
    //RETORNA UMA INSTANCIA DO FIREBASE STORAGE
    public static StorageReference getFirebaseStorage(){
        if(mStorage == null){
            mStorage = FirebaseStorage.getInstance().getReference();
        }
        return mStorage;
    }
}
