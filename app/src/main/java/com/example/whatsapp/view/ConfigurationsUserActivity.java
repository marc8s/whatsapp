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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfigFirebase;
import com.example.whatsapp.helper.Permission;
import com.example.whatsapp.helper.UserFirebase;
import com.example.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
    private ImageView mImageViewUpdateName;
    private EditText mEditProfileName;
    private StorageReference mStorageReference;
    private String mIdUser;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations_user);
        //configurações iniciais
        mStorageReference = ConfigFirebase.getFirebaseStorage();
        mIdUser = UserFirebase.getIdUser();
        mUser = UserFirebase.getDataUser();

        //validar permissões
        Permission.validatePermissions(mPermissionsNeeded, this, 1);

        mImageButtonCamera = findViewById(R.id.imageButtonCamera);
        mImageButtonGallery = findViewById(R.id.imageButtonGallery);
        mCircleImageViewProfile = findViewById(R.id.circleImageViewProfilePic);
        mEditProfileName = findViewById(R.id.editTextTextPersonNameConfig);
        mImageViewUpdateName = findViewById(R.id.imageViewUpdateName);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle(R.string.config);
        //continuar funcionando em versões anteriores
        setSupportActionBar(toolbar);
        //alterar suporte bar adicionando o botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //recuperar dados usuario
        FirebaseUser user = UserFirebase.getUser();
        Uri url = user.getPhotoUrl();
        if(url != null){
            Glide.with(ConfigurationsUserActivity.this)
                    .load(url)
                    .into(mCircleImageViewProfile);
        }else{
            mCircleImageViewProfile.setImageResource(R.drawable.padrao);
        }
        mEditProfileName.setText(user.getDisplayName());

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

        mImageViewUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mEditProfileName.getText().toString();
                boolean result = UserFirebase.updateNameUser(name);
                if(result){
                    mUser.setName(name);
                    mUser.update();
                    Toast.makeText(ConfigurationsUserActivity.this, R.string.sucess_update_name, Toast.LENGTH_SHORT).show();
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
                    //recuperar dados da imagem
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImage = baos.toByteArray();

                    //salvar imagem no firebase
                    final StorageReference imageRef = mStorageReference
                            .child("images")
                            .child("profile")
                            .child(mIdUser + ".jpeg");
                    UploadTask uploadTask = imageRef.putBytes(dadosImage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfigurationsUserActivity.this, R.string.error_upload, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ConfigurationsUserActivity.this, R.string.success_upload, Toast.LENGTH_SHORT).show();
                            //atualizar foto
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    updateProfilePicUser(url);
                                }
                            });
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updateProfilePicUser(Uri url){
        boolean result = UserFirebase.updatePicUser(url);
        if(result){
            mUser.setProfilePic(url.toString());
            mUser.update();
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