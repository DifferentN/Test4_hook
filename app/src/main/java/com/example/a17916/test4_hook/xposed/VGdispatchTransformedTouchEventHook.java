package com.example.a17916.test4_hook.xposed;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;

public class VGdispatchTransformedTouchEventHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        MotionEvent event = (MotionEvent) param.args[0];
        ViewGroup VG = (ViewGroup) param.thisObject;
        View child = (View) param.args[2];


        Log.i("LZH","TF Name: "+VG.getClass().getName()+" child count "+VG.getChildCount());
        if(child!=null){
            Log.i("LZH","TF child "+child.getClass().getName());
        }
        int bits = (int) param.args[3];
        boolean cancel = (boolean) param.args[1];
        Log.i("LZH","TF bits "+bits+"TF cancel "+cancel);
        if(event==null){
            Log.i("LZH","TF MotionEvent is null");
        }else{
            Log.i("LZH","TF event x: "+event.getRawX()+" event y: "+event.getRawY());
        }
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        boolean isExecute = (boolean) param.getResult();
        if(isExecute){
            Log.i("LZH","TF executed");
        }else{
            Log.i("LZH","TF can't executed");
        }
//        super.afterHookedMethod(param);
    }
}
