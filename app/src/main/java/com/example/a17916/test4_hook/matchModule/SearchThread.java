package com.example.a17916.test4_hook.matchModule;


import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;

import com.example.a17916.test4_hook.matchModule.impl.TaoPiaoPiaoBookTicket;
import com.example.a17916.test4_hook.matchModule.impl.YiDaoUseCar;

import org.json.JSONObject;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

public class SearchThread {
    private Context context;
    public SearchThread(Context context){
        this.context = context;
    }


    //list中保存adapter的类名
    private static ArrayMap<String,List<Class>> tag2App;

    static{
        tag2App = new ArrayMap<>();
        //添加<tag--应用>映射
        ArrayList<Class> movieName = new ArrayList<>();
        movieName.add(TaoPiaoPiaoBookTicket.class);
        tag2App.put("电影名称",movieName);

        ArrayList<Class> place = new ArrayList<>();
        place.add(YiDaoUseCar.class);
        tag2App.put("地点",place);

    }

    public List<Intent> getIntent(String tag, JSONObject jsonObject){

        ArrayList<Intent> intents = new ArrayList<>();
        List<Intent> subIntents;
        List<TargetApk> apks = getTargetApkByTag(tag);
        if(apks==null||apks.isEmpty()){
            Log.i("LZH","apks is empty or null");
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
        List<Class> apkClazz = tag2App.get(tag);
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        IntentAdapter intentAdapter;
        TargetApk targetApk;
        if(apkClazz==null){
            Log.i("LZH","apkNames is null");
            return null;
        }
        for(Class clazz:apkClazz){
            try {
                intentAdapter = (IntentAdapter) clazz.newInstance();
                intentAdapter.setContext(context);
                targetApk = new TargetApk();
                targetApk.setTag2adapter(tag,intentAdapter);
                apks.add(targetApk);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return apks;
    }

}
