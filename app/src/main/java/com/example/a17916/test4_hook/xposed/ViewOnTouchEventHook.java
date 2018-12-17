package com.example.a17916.test4_hook.xposed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ViewOnTouchEventHook extends XC_MethodHook {
//    XC_LoadPackage.LoadPackageParam loadPackageParam;
//    public ViewOnTouchEventHook(XC_LoadPackage.LoadPackageParam loadPackageParam){
//        this.loadPackageParam = loadPackageParam;
//    }
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
//            Log.i("LZH","event: X: "+motionEvent.getRawX()+" y: "+motionEvent.getRawY());
        }
        View view = (View) param.thisObject;
        if(view instanceof EditText){
            Log.i("LZH","点击了EditText");
            Context context = view.getContext();
            Intent intent = new Intent();
            intent.setAction(MonitorActivityService.SAVE_EDIT_TEXT);
            intent.putExtra(MonitorActivityService.EDIT_TEXT,((EditText)view).getText());
            context.sendBroadcast(intent);
        }
    }
}
