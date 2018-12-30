package com.example.a17916.test4_hook.xposed;

import android.util.Log;
import android.view.ViewParent;

import de.robv.android.xposed.XC_MethodHook;

public class ViewRootPerformTraversalsHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//        super.afterHookedMethod(param);
//        ViewParent viewParent;
        Log.i("LZH","执行performTraversals");
    }
}
