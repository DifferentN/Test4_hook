package com.example.a17916.test4_hook.share;

import android.content.Context;
import android.content.SharedPreferences;

public class KeyPreference {
    private static KeyPreference keyPreference;
    private Context context;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    public static KeyPreference getInstance(Context context){
        if(keyPreference == null){
            keyPreference = new KeyPreference(context);
        }
        return  keyPreference;
    }
    public KeyPreference(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("key",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }
    public void write(String activityName,String key){
        editor.putString(activityName,key).commit();
    }
    public String read(String activityName){
        return sharedPreferences.getString(activityName,"");
    }
}
