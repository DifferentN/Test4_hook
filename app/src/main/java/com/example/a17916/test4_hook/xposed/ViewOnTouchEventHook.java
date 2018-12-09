package com.example.a17916.test4_hook.xposed;

import android.util.Log;
import android.view.MotionEvent;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ViewOnTouchEventHook extends XC_MethodHook {
    XC_LoadPackage.LoadPackageParam loadPackageParam;
    public ViewOnTouchEventHook(XC_LoadPackage.LoadPackageParam loadPackageParam){
        this.loadPackageParam = loadPackageParam;
    }
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//        super.afterHookedMethod(param);
        boolean hasRun = (boolean) param.getResult();
        MotionEvent motionEvent;
        if(hasRun){
            motionEvent = (MotionEvent) param.args[0];
            Log.i("LZH","event: X: "+motionEvent.getRawX()+" y: "+motionEvent.getRawY());
        }
    }
}
