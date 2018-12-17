package com.example.a17916.test4_hook.receive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.a17916.test4_hook.TestGenerateTemple.TestGenerateTemple;
import com.example.a17916.test4_hook.manageActivity.ActivityController;
import com.example.a17916.test4_hook.monitorService.MonitorActivityReceiver;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.view_data.MyViewNode;
import com.example.a17916.test4_hook.view_data.MyViewTree;

public class CreateTempleReceiver extends BroadcastReceiver {
    public static final String CREATE_TEMPLE = "CREATE_TEMPLE";
    private Activity targetActivity;
    private String selfActivityName;
    private String showActivityName = "";
    public CreateTempleReceiver(Activity activity){
        targetActivity = activity;
        selfActivityName = activity.getComponentName().getClassName();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case CREATE_TEMPLE:
                if(selfActivityName.compareTo(showActivityName)==0){
                    sendViewTree(targetActivity);
                }
            case MonitorActivityService.ON_RESUME_STATE:
                showActivityName = intent.getStringExtra(MonitorActivityService.OPENED_ACTIVITY_NAME);

        }
    }
    private void sendViewTree(Context context){

//        Uri saveLogUri = Uri.parse("content://com.example.a17916.test4_hook.provider/save_log");
        View mDecorView = targetActivity.getWindow().getDecorView();
        ComponentName componentName= targetActivity.getComponentName();
        MyViewTree viewTree = new MyViewTree(mDecorView,componentName.getPackageName(),componentName.getShortClassName());
        MyViewNode viewNode = viewTree.getViewNode();

        String acNames[] = componentName.getShortClassName().split("\\.");
        TestGenerateTemple testGenerateTemple = new TestGenerateTemple(componentName.getClassName(),componentName.getPackageName());
        testGenerateTemple.createTemplate(viewNode);

//        context.getContentResolver().query(saveLogUri,new String[]{targetActivity.getClass().getName(), context.getPackageName()},"tree",null,viewNode.toString());
    }
}
