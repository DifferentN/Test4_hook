package com.example.a17916.test4_hook.xposed;

import android.util.Log;
import android.view.MotionEvent;

import de.robv.android.xposed.XC_MethodHook;

public class RecyclerViewOnTouchEventHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        MotionEvent event = (MotionEvent) param.args[0];
//        if(event==null){
//            Log.i("LZH","MotionEvent is null");
//        }else{
//            Log.i("LZH","event x: "+event.getRawX()+" event y: "+event.getRawY());
//        }
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        boolean isExecute = (boolean) param.getResult();
//        if(isExecute){
//            Log.i("LZH","executed");
//        }else{
//            Log.i("LZH","can't executed");
//        }
//        super.afterHookedMethod(param);

    }
}
