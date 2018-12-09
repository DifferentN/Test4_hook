package com.example.a17916.test4_hook.xposed;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import de.robv.android.xposed.XC_MethodHook;

public class ViewGroupDispatchTouchEventHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        MotionEvent event = (MotionEvent) param.args[0];
        ViewGroup VG = (ViewGroup) param.thisObject;
        Log.i("LZH","VG Name: "+VG.getClass().getName());
        String action = getAction(event.getAction());
        if(action.compareTo("-1")==0){
            action = event.getAction()+"";
        }
        if(event==null){
            Log.i("LZH","VG MotionEvent is null");
        }else{
            Log.i("LZH","VG event x: "+event.getRawX()+" event y: "+event.getRawY()+" action: "+action);
        }
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        boolean isExecute = (boolean) param.getResult();
        if(isExecute){
            Log.i("LZH","VG executed");
        }else{
            Log.i("LZH","VG can't executed");
        }
//        super.afterHookedMethod(param);
    }

    private String getAction(int action){
        String res = "-1";
        switch (action){
            case MotionEvent.ACTION_DOWN:
                res = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                res = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                res = "ACTION_UP";
                break;
            case MotionEvent.ACTION_CANCEL:
                res = "ACTION_CANCEL";
                break;
        }
        return res;
    }
}
