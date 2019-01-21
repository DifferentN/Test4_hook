package com.example.a17916.test4_hook.xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;


import com.example.a17916.test4_hook.TempGenerateDataBase.GenerateDataService;
import com.example.a17916.test4_hook.monitorService.MonitorActivityReceiver;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.share.SavePreference;
import com.example.a17916.test4_hook.util.normal.AppUtil;

import java.lang.reflect.Field;
import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;

public class ActivityOnResumeHook extends XC_MethodHook {

    private long curTime = 0,preTime = 0;
    private HashMap<String ,Long> onResumeHash;

    public ActivityOnResumeHook() {
        super();
        onResumeHash = new HashMap<>();
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//        super.beforeHookedMethod(param);
        Activity activity = (Activity) param.thisObject;
//        ComponentName componentName = activity.getComponentName();

        ComponentName componentName = activity.getComponentName();

        Log.i("LZH","before resume "+componentName.getClassName());
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Activity activity = (Activity) param.thisObject;
        ComponentName componentName = activity.getComponentName();
        String activityName = componentName.getClassName();
        Log.i("LZH","after resume "+componentName.getClassName()+" pkName "+componentName.getPackageName());

        Intent intent = new Intent();
        intent.setAction(LocalActivityReceiver.currentActivity);
        Bundle bundle = new Bundle();
        bundle.putString("showActivity",componentName.getClassName());
        bundle.putString("curPackage",componentName.getPackageName());
        bundle.putString("curApp",AppUtil.getAppName(activity));
        intent.putExtra("currentActivity",bundle);
        activity.sendBroadcast(intent);

        Intent broad = new Intent();
        broad.setAction(MonitorActivityService.ON_RESUME_STATE);
        broad.putExtra(MonitorActivityService.RESUME_PACKAGE_NAME,componentName.getPackageName());
        broad.putExtra(MonitorActivityService.RESUME_ACTIVITY_NAME,componentName.getClassName());
        broad.putExtra(MonitorActivityService.RESUME_APP_NAME,AppUtil.getAppName(activity));
        activity.sendBroadcast(broad);

        //测试中，保存打开应用的Intent

        Intent saveIntent = new Intent();

        saveIntent.setAction(GenerateDataService.SAVE_INTENT);
        Intent tempIntent = tempSaveIntent(activity.getIntent(),activity);
        if(tempIntent==null){
            tempIntent = activity.getIntent();
        }
        saveIntent.putExtra(GenerateDataService.NEED_SAVE_INTENT,tempIntent);
        saveIntent.putExtra(GenerateDataService.ACTIVITY_NAME,activity.getComponentName().getClassName());
        activity.sendBroadcast(saveIntent);


    }
    private Intent tempSaveIntent(Intent intent, Activity activity){
        ComponentName componentName = intent.getComponent();
        String activityName = componentName.getClassName();
        if(!activityName.equals("com.douban.frodo.subject.activity.LegacySubjectActivity")){
            return null;
        }
        SavePreference savePreference = SavePreference.getInstance(activity.getApplicationContext());
        String part1 = intent.getStringExtra("page_uri");
        Log.i("LZH","page_uri: "+part1);
        String part2 = intent.getStringExtra("subject_uri");
        Log.i("LZH","subject_uri: "+part2);
        Object o1 = intent.getSerializableExtra("com.douban.frodo.SUBJECT");
        Object o2 = intent.getParcelableExtra("com.douban.frodo.SUBJECT");
        if(o1!=null){
            printOnject(o1);
            Log.i("LZH","is Serializable");
        }else if(o2!=null){
            printOnject(o2);
            Log.i("LZH","is Parcelable");
        }else{
            Log.i("LZH","o1 o2 is null");
        }

        Intent newIntent = new Intent();
        newIntent.setComponent(componentName);
        newIntent.putExtra("page_uri",part1);
        newIntent.putExtra("subject_uri",part2);
        newIntent.putExtra("appName",AppUtil.getAppName(activity));
        intent.putExtra("appName",AppUtil.getAppName(activity));
        savePreference.writeIntent(activityName,newIntent);

        return newIntent;
    }
    private void printOnject(Object o){
        Class clazz = null;
        try {
            clazz = Class.forName("com.douban.frodo.subject.model.subject.Subject");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field field = null;
        try {
            field = clazz.getField("title");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
        try {
            String value = (String) field.get(o);
            Log.i("LZH","title: "+value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
