package com.example.a17916.test4_hook.receive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.example.a17916.test4_hook.util.activityView.ActivityCache;
import com.example.a17916.test4_hook.view_data.MyViewNode;
import com.example.a17916.test4_hook.view_data.MyViewTree;


public class ActivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("sendViewTree")){
//            Log.i("LZH","send View Tree");
//            sendViewTree(context);
        }else if(action.equals("clickFloatBt")){

        }
    }
    private void sendViewTree(Context context){
        ActivityCache activityCache = ActivityCache.getInstance();
        ActivityCache.ActivityNode activityNode = activityCache.getFirst();
        Uri saveLogUri = Uri.parse("content://com.example.a17916.test4_hook.provider/save_log");
        View mDecorView = activityNode.getActivity().getWindow().getDecorView();
        ComponentName componentName = activityNode.getActivity().getComponentName();
        MyViewTree viewTree = new MyViewTree(mDecorView,componentName.getPackageName());
        MyViewNode viewNode = viewTree.getViewNode();

        context.getContentResolver().query(saveLogUri,new String[]{activityNode.getFlagName(), context.getPackageName()},"tree",null,JSONObject.toJSONString(viewNode));
    }
}
