package com.example.whatsapp.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

import com.example.whatsapp.R;
import com.example.whatsapp.helper.Permission;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfigurationsUserActivity extends AppCompatActivity {

    private String[] mPermissionsNeeded = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private ImageButton mImageButtonCamera, mImageButtonGallery;
    private static final int mSELECTION_CAMERA = 100;
    private static final int mSELECTION_GALLERY = 200;
    private CircleImageView mCircleImageViewProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations_user);

        Permission.validatePermissions(mPermissionsNeeded, this, 1);

        mImageButtonCamera = findViewById(R.id.imageButtonCamera);
        mImageButtonGallery = findViewById(R.id.imageButtonGallery);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfilePic);

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
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(i, mSELECTION_GALLERY);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap image = null;

            try {
                switch (requestCode){
                    case mSELECTION_CAMERA:
                        image = (Bitmap) data.getExtras().get("data");
                        break;
                    case mSELECTION_GALLERY:
                        Uri localImageSelected = data.getData();
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(), localImageSelected);
                        break;
                }
                if(image != null){
                    mCircleImageViewProfile.setImageBitmap(image);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
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