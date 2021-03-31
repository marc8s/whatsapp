package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName, mEmail, mPassword;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.editTextTextPersonName);
        mEmail = findViewById(R.id.editTextTextEmailAddress2);
        mPassword = findViewById(R.id.editTextTextPassword2);
        mRegister = findViewById(R.id.buttonRegister);
    }
}