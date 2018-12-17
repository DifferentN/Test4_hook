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
                myHandler.onResumeActivity(this,intent);
                pkName = intent.getStringExtra(MonitorActivityService.OPENED_PACKAGE_NAME);
                Log.i("LZH","resume pkName: "+pkName);
                activityName = intent.getStringExtra(MonitorActivityService.OPENED_ACTIVITY_NAME);

                hashMap = record.get(pkName);
                if(hashMap==null){
                    hashMap = new HashMap<>();
                    record.put(pkName,hashMap);
                }
                hashMap.put(activityName,true);

                if(currentPackageName==null||currentPackageName.compareTo(pkName)!=0){
                    currentPackageName = pkName;
                    resetState();
                    Log.i("LZH","切换了应用");
                }
                liveActivity.add(activityName);
                needOpenActivity(pkName,activityName);
                needPlayEvent(activityName);
                break;
            case MonitorActivityService.ON_DESTROY_STATE:
                myHandler.onDestroyActivity(this,intent);
                pkName = intent.getStringExtra(MonitorActivityService.DESTROY_PACKAGE_NAME);
                activityName = intent.getStringExtra(MonitorActivityService.DESTROY_ACTIVITY_NAME);
                Log.i("LZH","destroy pkName: "+pkName);
                hashMap = record.get(pkName);
                if(hashMap!=null&&hashMap.get(pkName)!=null){
                    hashMap.remove(pkName);
                }
                break;

        }

    }


    private void needOpenActivity(String PKName,String activityName) {
        hashMap = record.get(PKName);
        if(hashMap.size()>=2&&toOpenActivity&&activityName.compareTo(targetActivityName)!=0){
            openActivityByIntent();
            toOpenActivity = false;
        }
    }

    private void resetState(){
        resumeActivityTime = 0;
        destroyActivityTime = 0;
        liveActivity.clear();
    }

    private void needPlayEvent(String activityName){
        Log.i("LZH","isPlayEvent: "+isPlayEvent+"activityName: "+targetActivityName);
        if(!isPlayEvent||activityName.compareTo(targetActivityName)!=0){
            return;
        }
        //检查是否要输入搜索词
        needInputText();

        Intent intent = new Intent();
        intent.setAction(LocalActivityReceiver.INPUT_EVENT);
        intent.putExtra(LocalActivityReceiver.EVENTS,eventBytes);
        intent.putExtra(LocalActivityReceiver.fromActivityPlay,targetActivityName);
        service.sendBroadcast(intent);
        isPlayEvent = false;
    }

    private void needInputText(){
        Log.i("LZH","发送"+text);
        if(text==null){
            return;
        }
        Intent intent = new Intent();
        intent.setAction(LocalActivityReceiver.INPUT_TEXT);
        intent.putExtra(LocalActivityReceiver.TEXT_KEY,text);
        intent.putExtra(LocalActivityReceiver.fromActivityPlay,targetActivityName);
        service.sendBroadcast(intent);
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
    private byte[] getMotionEvents(String activityName,String text){
        if(text==null){
            Log.i("LZH","text为null，无法获得点击事件");
            return null;
        }
        String key = activityName+"/"+text;
        return savePreference.readMotionEventsByte(key);
    }

    public void setMotionEvent(String activityName,String key){
        text = key;
        eventBytes = getMotionEvents(activityName,key);
        if(eventBytes==null){
            Log.i("LZH","无法获取点击事件,activityName: "+activityName+" key: "+key);
            return;
        }
        isPlayEvent = true;
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
    public void operationStartActivity(Intent intent) {

    }

    @Override
    public void operationReplayInputEvent(String text) {

    }

    @Override
    public void operationReplayMotionEvent(byte[] events) {

    }
}
