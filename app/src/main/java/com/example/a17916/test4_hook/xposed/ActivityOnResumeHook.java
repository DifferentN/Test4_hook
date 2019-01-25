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
import com.example.a17916.test4_hook.util.normal.IntentUtil;
import com.example.a17916.test4_hook.util.normal.ViewUtil;

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

        IntentUtil.showKeyValue(activity.getIntent().getExtras());
//        Log.i("LZH","activity: "+componentName.getClassName()+" 有的View数 "+ViewUtil.getViewCountInActivity(activity.getWindow().getDecorView()));

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
        Intent tempIntent = activity.getIntent();

        saveIntent.putExtra(GenerateDataService.NEED_SAVE_INTENT,tempIntent);
        saveIntent.putExtra(GenerateDataService.ACTIVITY_NAME,activity.getComponentName().getClassName());
        activity.sendBroadcast(saveIntent);

    }
}
