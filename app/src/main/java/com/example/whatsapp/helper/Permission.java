package com.example.whatsapp.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permission {
    public static boolean validatePermissions(String[] permissions, Activity activity, int requestCode){
        //apartir dessa api que passou a ser necessario esse tratamento
        if(Build.VERSION.SDK_INT >= 23){
            List<String> listPermissions = new ArrayList<>();
            //percorre as permissões pra ver se ja foi liberada
            for(String permission : permissions){
                Boolean hadPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if(!hadPermission){
                    listPermissions.add(permission);
                }
            }
            //se estiver vazia não precisa solicitar permissão
            if(listPermissions.isEmpty()){
                return true;
            }else {
                //solicita permissão
                String[] newPermissions = new String[listPermissions.size()];
                listPermissions.toArray(newPermissions);
                ActivityCompat.requestPermissions(activity, newPermissions, requestCode);
            }
        }

        return true;
    }
}
