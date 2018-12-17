package com.example.a17916.test4_hook.xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;

public class TestActivityStartActivityForResult extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//        super.afterHookedMethod(param);
        Activity activity = (Activity) param.thisObject;
        ComponentName componentName = activity.getComponentName();
        Log.i("LZH","要求返回的方式: "+componentName.getClassName());
    }
}
