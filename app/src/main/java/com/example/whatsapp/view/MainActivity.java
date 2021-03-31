package com.example.whatsapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfigFirebase;
import com.example.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class MainActivity extends AppCompatActivity {

    private TextView mRegister;
    private EditText mEmail, mPassword;
    private Button mLogin;
    private FirebaseAuth mAuthentication;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegister = findViewById(R.id.texviewRegister);
        mEmail = findViewById(R.id.editTextTextEmailAddress);
        mPassword = findViewById(R.id.editTextTextPassword);
        mLogin = findViewById(R.id.buttonLogin);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCamps()){
                    validateLogin();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyUserLogged();
    }

    public boolean checkCamps(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if(!email.isEmpty()){
            if (!password.isEmpty()){
                mUser = new User();
                mUser.setEmail(email);
                mUser.setPassword(password);
                return true;
            }else{
                Toast.makeText(MainActivity.this, R.string.password_missing, Toast.LENGTH_SHORT).show();
                return false;
            }

        }else{
            Toast.makeText(MainActivity.this, R.string.email_missing, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void validateLogin(){
        mAuthentication = ConfigFirebase.getFirebaseAuthentication();
        mAuthentication.signInWithEmailAndPassword(
                mUser.getEmail(),
                mUser.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    openHome();
                }else{
                    String exception = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        exception = getString(R.string.user_unregisted);
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = getString(R.string.email_password_wrong);
                    }catch (Exception e){
                        exception = getString(R.string.error_login) + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this,
                            exception,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void verifyUserLogged(){
        mAuthentication = ConfigFirebase.getFirebaseAuthentication();
        //mAuthentication.signOut();
        if(mAuthentication.getCurrentUser() != null){
            openHome();
        }
    }

    public void openHome(){
        startActivity(new Intent(this, HomeActivity.class));
        //finish();
    }


}