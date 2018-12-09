package com.example.a17916.test4_hook.xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;

import de.robv.android.xposed.XC_MethodHook;

public class ActivityOnResumeHook extends XC_MethodHook {
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
        Log.i("LZH","after resume "+componentName.getClassName()+" time "+SystemClock.currentThreadTimeMillis());

        Intent intent = new Intent();
        intent.setAction(LocalActivityReceiver.currentActivity);
        Bundle bundle = new Bundle();
        bundle.putString("showActivity",componentName.getClassName());
        bundle.putString("curPackage",componentName.getPackageName());
        intent.putExtra("currentActivity",bundle);
        activity.sendBroadcast(intent);

        //给服务发送打开命令，打开指定activity
        Intent broad = new Intent();
        broad.setAction(MonitorActivityService.openActivity);
        activity.sendBroadcast(broad);

        //测试中，保存打开应用的Intent
        Intent saveIntent = new Intent();
        saveIntent.setAction(MonitorActivityService.saveIntent);
        saveIntent.putExtra("opendActivityIntent",activity.getIntent());
        saveIntent.putExtra("openActivityName",activity.getComponentName().getClassName());
        activity.sendBroadcast(saveIntent);

    }
}
