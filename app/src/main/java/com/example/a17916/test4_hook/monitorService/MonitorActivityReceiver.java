package com.example.a17916.test4_hook.monitorService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;

import com.example.a17916.test4_hook.manageActivity.ActivityController;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.share.SavePreference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitorActivityReceiver extends BroadcastReceiver implements Operation{
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

    private ArrayMap<String,Boolean> mapIsOpened;

    private boolean isPlayEvent = false;
    private String text;
    private byte[] eventBytes;

    //记录App打开的页面，当>=2个时，打开目标页面
    private ArrayMap<String,HashMap<String,Boolean>> record;
    private HashMap<String,Boolean> hashMap;
    private MyActivityHandler myHandler;
    public MonitorActivityReceiver(Service service){
        this.service = service;
        savePreference = SavePreference.getInstance(service);
        liveActivity = new ArrayList<>();
        mapIsOpened = new ArrayMap<>();
        record = new ArrayMap<>();
        myHandler = MyActivityHandler.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String pkName = "",activityName = "";
        switch (action){
            case MonitorActivityService.ON_CREATE_STATE:
                myHandler.onCreateActivity(this,intent);
                break;
            case MonitorActivityService.ON_RESUME_STATE:
//                Log.i("LZH","收到显示的页面");
                myHandler.onResumeActivity(this,intent);
                break;
            case MonitorActivityService.ON_DESTROY_STATE:
                myHandler.onDestroyActivity(this,intent);
                break;
            case MonitorActivityService.ON_DRAW:
                myHandler.onDrawView(this,intent);
        }

    }



    private void resetState(){
        resumeActivityTime = 0;
        destroyActivityTime = 0;
        liveActivity.clear();
    }

    public void setTargetIntent(Intent intent){
        tarIntent = intent;
        ComponentName componentName = intent.getComponent();
        targetPackageName = componentName.getPackageName();
        targetActivityName = componentName.getClassName();
        resetState();
        mapIsOpened.put(targetActivityName,false);
        toOpenActivity = true;
    }

    @Override
    public void operationStartActivity(Intent intent,String fromActivity) {
        Intent broadIntent = new Intent();
        broadIntent.putExtra(LocalActivityReceiver.TARGET_INTENT,intent);
        broadIntent.putExtra(LocalActivityReceiver.fromActivityStart,fromActivity);
        broadIntent.setAction(LocalActivityReceiver.openTargetActivityByIntent);
        service.sendBroadcast(broadIntent);
    }

    @Override
    public void operationReplayInputEvent(String text,String fromActivity) {
        Intent broadIntent = new Intent();
        broadIntent.setAction(LocalActivityReceiver.INPUT_TEXT);
        broadIntent.putExtra(LocalActivityReceiver.TEXT_KEY,text);
        broadIntent.putExtra(LocalActivityReceiver.fromActivityPlay,fromActivity);
        service.sendBroadcast(broadIntent);
    }

    @Override
    public void operationReplayMotionEvent(byte[] events,String fromActivity) {
        Intent broadIntent = new Intent();
        broadIntent.setAction(LocalActivityReceiver.INPUT_EVENT);
        broadIntent.putExtra(LocalActivityReceiver.EVENTS,events);
        broadIntent.putExtra(LocalActivityReceiver.fromActivityPlay,fromActivity);
        service.sendBroadcast(broadIntent);
    }
}
