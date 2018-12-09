package com.example.a17916.test4_hook.xposed.hookFragment;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;

public class TransactionReplaceHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        Log.i("LZH","fragment replace");
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//        super.afterHookedMethod(param);
    }
}
