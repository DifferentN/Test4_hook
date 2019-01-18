package com.example.a17916.test4_hook.manageActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.a17916.test4_hook.TestGenerateTemple.PageResult;
import com.example.a17916.test4_hook.activity.showResult.AnalyseResultActivity;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;

import java.util.ArrayList;

public class ControllerReciver extends BroadcastReceiver {
    private MonitorActivityService.TransportBinder transportBinder = null;
    private MonitorActivityService monitorService;
    private String packageName = "";
    private String text;
    private Intent tarIntent;
    public ControllerReciver(MonitorActivityService.TransportBinder transportBinder){
        this.transportBinder = transportBinder;
        monitorService = transportBinder.getService();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case ActivityController.SEND_ACTIVITY_INTENT:
                packageName = intent.getStringExtra(ActivityController.PK_NAME);
                tarIntent = intent.getParcelableExtra(ActivityController.TARGET_INTENT);
                monitorService.openActivity(packageName,tarIntent);
                break;
            case ActivityController.SEND_INTENT_MOTION:
//                packageName = intent.getStringExtra(ActivityController.PK_NAME);
//                tarIntent = intent.getParcelableExtra(ActivityController.TARGET_INTENT);
//                text = intent.getStringExtra(ActivityController.TEXT);
//                monitorService.openActivityWithMotionEvent(packageName,tarIntent,text);
                break;
            case ActivityController.OPEN_ACTIVITY:
                packageName = intent.getStringExtra(ActivityController.PK_NAME);
                monitorService.openApp(packageName);
                break;
            case ActivityController.OPEN_ANALYSE_ACTIVITY:
                ArrayList<PageResult> pageResults = intent.getParcelableArrayListExtra(AnalyseResultActivity.PageResult);
                Intent intent1 = new Intent();
                ComponentName componentName = new ComponentName("com.example.a17916.test4_hook","com.example.a17916.test4_hook.activity.showResult.AnalyseResultActivity");
                intent1.setComponent(componentName);
                intent1.putParcelableArrayListExtra(AnalyseResultActivity.PageResult,pageResults);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                monitorService.startActivity(intent1);
                break;
        }
    }
}
