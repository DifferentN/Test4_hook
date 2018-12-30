package com.example.a17916.test4_hook.xposed;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import de.robv.android.xposed.XC_MethodHook;

public class VGgetTouchTargetHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Object o = param.getResult();
        View view = (View) param.args[0];
        if(o==null){
            Log.i("LZH",view.getClass().getName()+" touchTarget is null");
        }else{
            Log.i("LZH",view.getClass().getName()+" touchTarget is not null");
        }
//        super.afterHookedMethod(param);
    }
}
