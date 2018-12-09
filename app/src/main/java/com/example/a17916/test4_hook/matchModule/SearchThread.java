package com.example.a17916.test4_hook.matchModule;


import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchThread {
    private Context context;
    public SearchThread(Context context){
        this.context = context;
    }


    //list中保存adapter的类名
    private static ArrayMap<String,List<String>> tag2App;

    static{
        tag2App = new ArrayMap<>();
        //添加<tag--应用>映射
        ArrayList<String> movieName = new ArrayList<>();
        movieName.add("com.example.a17916.test4_hook.matchModule.impl.MaoYanBookTicket");
        tag2App.put("电影名称",movieName);

        ArrayList<String> place = new ArrayList<>();
        place.add("com.example.a17916.test4_hook.matchModule.impl.YiDaoUseCar");
        tag2App.put("",place);

    }

    public List<Intent> getIntent(String tag, JSONObject jsonObject){
        ArrayList<Intent> intents = new ArrayList<>();
        List<Intent> subIntents;
        List<TargetApk> apks = getTargetApkByTag(tag);
        if(apks==null){
            Log.i("LZH","apks is null");
            return null;
        }
        for(TargetApk apk:apks){
            subIntents = apk.getIntentsByTag(tag,jsonObject);
            intents.addAll(subIntents);
        }
        return intents;
    }

    private List<TargetApk> getTargetApkByTag(String tag){
        ArrayList<TargetApk> apks = new ArrayList<>();
        List<String> apkNames = tag2App.get(tag);
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        IntentAdapter intentAdapter;
        TargetApk targetApk;
        if(apkNames==null){
            Log.i("LZH","apkNames is null");
            return null;
        }
        for(String name:apkNames){
            try {
                intentAdapter = (IntentAdapter) cl.loadClass(name).newInstance();
                intentAdapter.setContext(context);
                targetApk = new TargetApk();
                targetApk.setTag2adapter(tag,intentAdapter);
                apks.add(targetApk);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return apks;
    }

}
