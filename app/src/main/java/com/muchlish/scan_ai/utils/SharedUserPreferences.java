package com.muchlish.scan_ai.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedUserPreferences {
    public static final String SCANAI_APP = "SCANAI_APP";

    public static final String TOKEN = "TOKEN";

    public static final String IS_LOGIN = "IS_LOGIN";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedUserPreferences(Context context){
        sp = context.getSharedPreferences(SCANAI_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveToken(String value){
        spEditor.putString(TOKEN, value);
        spEditor.commit();
    }

    public void setIsLogin(boolean value){
        spEditor.putBoolean(IS_LOGIN, value);
        spEditor.commit();
    }

    public String getToken(){
        return sp.getString(TOKEN, "");
    }


    public Boolean isLogin(){
        return sp.getBoolean(IS_LOGIN, false);
    }
}
