package com.example.a17916.test4_hook.xposed.hookFragment;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;


//xposed 不能hook在APP启动后new 的对象
public class FragmentOnCreateViewHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        Log.i("LZH","load fragment view");
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Log.i("LZH","load fragment view");
//        super.afterHookedMethod(param);
    }
}
