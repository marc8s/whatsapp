package com.example.whatsapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Permission;

public class ConfigurationsUserActivity extends AppCompatActivity {

    private String[] mPermissionsNeeded = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private ImageButton mImageButtonCamera, mImageButtonGallery;
    private static final int mSELECTION_CAMERA = 100;
    private static final int mSELECTION_GALLERY = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations_user);

        Permission.validatePermissions(mPermissionsNeeded, this, 1);

        mImageButtonCamera = findViewById(R.id.imageButtonCamera);
        mImageButtonGallery = findViewById(R.id.imageButtonGallery);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.config);
        //continuar funcionando em versões anteriores
        setSupportActionBar(toolbar);
        //alterar suporte bar adicionando o botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(i, mSELECTION_CAMERA);
                }

            }
        });

        mImageButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissionResult : grantResults){
            if(permissionResult == PackageManager.PERMISSION_DENIED){
                alertValidatePermission();
            }
        }
    }

    private void alertValidatePermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_denied);
        builder.setMessage(R.string.message_permission_denied);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}