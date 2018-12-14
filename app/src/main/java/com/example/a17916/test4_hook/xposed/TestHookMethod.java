package com.example.a17916.test4_hook.xposed;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;

public class TestHookMethod extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//        Log.i("LZH","startActivity_Hook");
    }

    //launcher 启动其他应用时调用 startActivityForResult
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        Intent intent = (Intent) param.getResult();
        Log.i("LZH","检测到Intent传送,传送目标："+intent.getComponent().getClassName());
    }
}
