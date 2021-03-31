package com.example.whatsapp.helper;

import android.util.Base64;

public class Base64Custom {
    public static String encodeBase64(String textDecode){
        //a expressão regular remove os caracteres invalidos
        return Base64.encodeToString(textDecode.getBytes(), Base64.NO_WRAP)
                .replaceAll("(\\n|\\r)", "");
    }
    public static String decodeBase64(String textEncode){
        return new String(Base64.decode(textEncode, Base64.DEFAULT));
    }
}
