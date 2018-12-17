package com.example.a17916.test4_hook.receive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;

public class InputTextReceiver extends BroadcastReceiver {
//    2131690018
    public static final String INPUT_TEXT = "inputText";

    private Activity activity;

    private View decor;

    private EditText editText;
    public InputTextReceiver(Activity activity){
        this.activity = activity;
        decor = activity.getWindow().getDecorView();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(!activity.getClass().getName().equals("com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity")){
            return;
        }
        switch (action){
            case InputTextReceiver.INPUT_TEXT:
                editText = findEditText(decor);
                if(editText!=null){
                    editText.setText("北京房山");
                }else{
                    Log.i("LZH","找不到EditText");
                }
                break;
        }
    }
    private EditText findEditText(View view){
        ArrayList<View> list = new ArrayList<>();
        list.add(view);
        ViewGroup viewGroup;
        View cur;
        while(!list.isEmpty()){
            cur = list.remove(0);
            if(cur instanceof EditText){
                list.clear();
                return (EditText) cur;
            }else if(cur instanceof ViewGroup){
                viewGroup = (ViewGroup) cur;
                for(int i=0;i<viewGroup.getChildCount();i++){
                    cur = viewGroup.getChildAt(i);
                    list.add(cur);
                }
            }
        }
        return null;
    }
}
