package com.example.a17916.test4_hook.monitorService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.share.SavePreference;

import java.util.ArrayList;

public class MonitorActivityReceiver extends BroadcastReceiver {

    public static final String openActivity = "openActivity";
    public static final String opened = "hasOpened";
    public static final String openByIntent = "openActivityByIntent";
    public static final String openByIntentInfo = "openActivityByIntentInfo";

    public static final String ON_CREATE_STATE = "onCreateActivity";
    public static final String ON_RESUME_STATE = "onResumeActivity";
    public static final String ON_DESTROY_STATE = "onDestroyActivity";

    public static final String OPENED_PACKAGE_NAME = "openedPackageName";
    public static final String OPENED_ACTIVITY_NAME = "openedActivityName";
    public static final String DESTROY_PACKAGE_NAME = "destroyPackageName";
    public static final String DESTROY_ACTIVITY_NAME = "destroyActivityName";

    private String targetActivityName;
    private String targetPackageName;
    private String currentPackageName;
    private int resumeActivityTime;
    private int destroyActivityTime;
    private ArrayList<String> liveActivity;// 表示当前应用存活的Activity
    private boolean toOpenActivity = false;
    private boolean isOpenedActivity = false;

    private static String openWay = "";

    private Service service;

    private Intent tarIntent;

    private SavePreference savePreference;

    public MonitorActivityReceiver(Service service){
        this.service = service;
        savePreference = SavePreference.getInstance(service);
        liveActivity = new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String pkName = "",activityName = "";
//        switch (action){
//            case MonitorActivityService.openActivity:
//                openActivity();
//                break;
//            case MonitorActivityService.openByIntent:
//                openWay = openByIntent;
//
//                recordTargetActivityInfo(intent);
//                break;
//            case MonitorActivityService.openByIntentInfo:
//                openWay = openByIntentInfo;
//                break;
//            case MonitorActivityService.opened:
//                openWay = "";
//                break;
//            case MonitorActivityReceiver.testSave:
//                tarIntent = intent.getParcelableExtra("testIntent");
//                break;
//        }
        switch (action){
            case MonitorActivityReceiver.ON_CREATE_STATE:

                break;
            case MonitorActivityReceiver.ON_RESUME_STATE:
                pkName = intent.getStringExtra(OPENED_PACKAGE_NAME);
                activityName = intent.getStringExtra(OPENED_ACTIVITY_NAME);
                if(currentPackageName==null||currentPackageName.compareTo(pkName)!=0){
                    currentPackageName = pkName;
                    resetState();
                    Log.i("LZH","切换了应用");
                }
                liveActivity.add(activityName);
                if(activityName.compareTo(targetActivityName)==0){
                    toOpenActivity = false;
                    isOpenedActivity = true;
                }
                resumeActivityTime++;
                needOpenActivity();
                break;
            case MonitorActivityReceiver.ON_DESTROY_STATE:
                pkName = intent.getStringExtra(DESTROY_PACKAGE_NAME);
                activityName = intent.getStringExtra(DESTROY_ACTIVITY_NAME);

                break;


        }

    }

    private void needOpenActivity() {
        Log.i("LZH","toOpenActivity: "+toOpenActivity+" "+(resumeActivityTime-destroyActivityTime));
        if(toOpenActivity&&resumeActivityTime==2){
            openActivityByIntent();
        }
    }

    private void resetState(){
        resumeActivityTime = 0;
        destroyActivityTime = 0;
        liveActivity.clear();
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


    private void openActivityByIntent() {
        if(tarIntent==null){
            Log.i("LZH","目标Intent为null");
            return;
        }
        Log.i("LZH","目标Intent不为null");
        Intent openActivity = new Intent();
        openActivity.putExtra(LocalActivityReceiver.TARGET_INTENT,tarIntent);
        openActivity.putExtra(LocalActivityReceiver.fromActivityStart,liveActivity.get(liveActivity.size()-1));
        openActivity.setAction(LocalActivityReceiver.openTargetActivityByIntent);
        service.sendBroadcast(openActivity);
    }
    private byte[] getMotionEvents(String activityName){
        String key = activityName+"/MotionEvent";
        return savePreference.readMotionEventsByte(key);
    }

    public void setTargetIntent(Intent intent){
        tarIntent = intent;
        ComponentName componentName = intent.getComponent();
        targetPackageName = componentName.getPackageName();
        targetActivityName = componentName.getClassName();
        resetState();
        toOpenActivity = true;
        isOpenedActivity = false;
    }
}
