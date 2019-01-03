package com.example.a17916.test4_hook.xposed;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;

import de.robv.android.xposed.XC_MethodHook;

public class EditTextSetTextHook extends XC_MethodHook {
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//        super.afterHookedMethod(param);
        View view = (View) param.thisObject;
        Context context = view.getContext();

        String text = null;
        EditText editText;
        if(view instanceof EditText){
            editText = (EditText) view;
            text = editText.getText().toString();
            Log.i("LZH","用setText输入了数据: "+text);
        }

        if(text==null||text.length()<1){
            return;
        }
        Intent intent = new Intent();
        intent.setAction(MonitorActivityService.INPUTED_TEXT);
        context.sendBroadcast(intent);
    }
}
