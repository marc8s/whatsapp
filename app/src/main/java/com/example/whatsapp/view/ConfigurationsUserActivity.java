package com.example.whatsapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.whatsapp.R;

public class ConfigurationsUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations_user);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.config);
        //continuar funcionando em versões anteriores
        setSupportActionBar(toolbar);
        //alterar suporte bar adicionando o botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}