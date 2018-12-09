package com.example.a17916.test4_hook.monitorService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.share.SavePreference;

public class MonitorActivityReceiver extends BroadcastReceiver {

    public static final String openActivity = "openActivity";
    public static final String opened = "hasOpened";
    public static final String openByIntent = "openActivityByIntent";
    public static final String openByIntentInfo = "openActivityByIntentInfo";
    public static final String testSave = "testSave";

    private static String openWay = "";

    private String targetActivityName;
    private String targetPackageName;

    private Service service;

    private Intent tarIntent;

    private SavePreference savePreference;

    public MonitorActivityReceiver(Service service){
        this.service = service;
        savePreference = SavePreference.getInstance(service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case MonitorActivityService.openActivity:
                openActivity();
                break;
            case MonitorActivityService.openByIntent:
                openWay = openByIntent;

                recordTargetActivityInfo(intent);
                break;
            case MonitorActivityService.openByIntentInfo:
                openWay = openByIntentInfo;
                break;
            case MonitorActivityService.opened:
                openWay = "";
                break;
            case MonitorActivityReceiver.testSave:
                tarIntent = intent.getParcelableExtra("testIntent");
                break;
        }
    }

    private void openActivity() {
        switch (openWay){
            case openByIntent:
                openActivityByIntent();
                break;
            case openByIntentInfo:
                break;
            case opened:
                break;
        }
    }

    private void recordTargetActivityInfo(Intent intent){
        tarIntent = intent.getParcelableExtra("tarIntent");
        targetPackageName = intent.getStringExtra(MonitorActivityService.targetPackageName);
        targetActivityName = intent.getStringExtra(MonitorActivityService.targetActivityName);
    }

    private void openActivityByIntent() {
//        Intent intent = getActivityIntent(targetActivityName);
        if(tarIntent==null){
            Log.i("LZH","Intent is null");
            return;
        }
        Intent openActivity = new Intent();
//        ComponentName componentName = new ComponentName(targetPackageName,targetActivityName);
//        tarIntent.setComponent(componentName);
        Bundle bundle = new Bundle();
        openActivity.putExtra("opendActivityIntent",tarIntent);
        byte [] bytes = getMotionEvents(targetActivityName);
//        if(bytes==null){
//            Log.i("LZH","can't open activity");
//        }
        bundle.putByteArray("motionEvents",bytes);
        openActivity.putExtra("event",bundle);

        openActivity.putExtra(MonitorActivityService.targetPackageName,targetPackageName);
        openActivity.putExtra(MonitorActivityService.targetActivityName,targetActivityName);
        openActivity.setAction(LocalActivityReceiver.openTargetActivityByIntent);
        service.sendBroadcast(openActivity);
    }
    private byte[] getMotionEvents(String activityName){
        String key = activityName+"/MotionEvent";
        return savePreference.readMotionEventsByte(key);
    }

    public void setTargetIntent(Intent intent){
        tarIntent = intent;
    }
}
