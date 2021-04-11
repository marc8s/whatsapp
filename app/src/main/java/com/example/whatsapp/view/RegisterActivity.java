package com.example.whatsapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfigFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UserFirebase;
import com.example.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class RegisterActivity extends AppCompatActivity {

    private EditText mName, mEmail, mPassword;
    private Button mRegister;
    private User mUser;
    private FirebaseAuth mAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.editTextTextPersonName);
        mEmail = findViewById(R.id.editTextTextEmailAddress2);
        mPassword = findViewById(R.id.editTextTextPassword2);
        mRegister = findViewById(R.id.buttonRegister);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCamps()){
                    registerNewUser();
                }
            }
        });
    }

    public boolean checkCamps(){
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if(!name.isEmpty()){
            if(!email.isEmpty()){
                if (!password.isEmpty()){
                    mUser = new User();
                    mUser.setName(name);
                    mUser.setEmail(email);
                    mUser.setPassword(password);
                    return true;
                }else{
                    Toast.makeText(RegisterActivity.this, R.string.password_missing, Toast.LENGTH_SHORT).show();
                    return false;
                }

            }else{
                Toast.makeText(RegisterActivity.this, R.string.email_missing, Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(RegisterActivity.this, R.string.name_missing, Toast.LENGTH_SHORT).show();
            return false;

        }
    }

    public void registerNewUser(){
        mAuthentication = ConfigFirebase.getFirebaseAuthentication();
        mAuthentication.createUserWithEmailAndPassword(
                mUser.getEmail(), mUser.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserFirebase.updateNameUser(mUser.getName());
                    try{
                        String idUser = Base64Custom.encodeBase64(mUser.getEmail());
                        mUser.setId(idUser);
                        mUser.save();
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    String exception = "";
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        exception = getString(R.string.password_weak);
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = getString(R.string.email_invalid);
                    }catch (FirebaseAuthUserCollisionException e){
                        exception = getString(R.string.account_duplicate);
                    }catch (Exception e){
                        exception = getString(R.string.error_register) + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterActivity.this,
                            exception,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}