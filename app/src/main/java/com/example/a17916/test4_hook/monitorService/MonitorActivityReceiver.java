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
import java.lang.reflect.Field;
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
    private boolean isInputedText = false;
    private String text;
    private byte[] eventBytes;
    private boolean recycle = false;

    //替换为延时打开
    //记录App打开的页面，当>=2个时，打开目标页面
    private ArrayMap<String,HashMap<String,Boolean>> record;
    //记录页面打开时间
    private HashMap<String,Long> hashMap;

    //延时打开
    private OnResumeRunnable onResumeRunnable = null;
    private Thread thread = null;

    private MyActivityHandler myHandler;
    public MonitorActivityReceiver(Service service){
        this.service = service;
        savePreference = SavePreference.getInstance(service);
        liveActivity = new ArrayList<>();
        mapIsOpened = new ArrayMap<>();
        record = new ArrayMap<>();
        myHandler = MyActivityHandler.getInstance();

        hashMap = new HashMap<>();
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
                Log.i("LZH","收到显示的页面");
//                tempSaveIntent();
//                if(!isRepeat(intent)){
//                    myHandler.onResumeActivity(this,intent);
//                }
                if(recycle){
                    onResumeRunnable = null;
                    recycle = false;
                }
                if(onResumeRunnable == null){
                    onResumeRunnable = new OnResumeRunnable(this,myHandler,this,intent);
                    thread = new Thread(onResumeRunnable);
                    thread.start();
                }else{
                    onResumeRunnable.setUpdate(true,this,intent);
                }

                break;
            case MonitorActivityService.ON_DESTROY_STATE:
                myHandler.onDestroyActivity(this,intent);
                break;
            case MonitorActivityService.INPUTED_TEXT:
                isInputedText = true;
            case MonitorActivityService.ON_DRAW:
                if(isInputedText){
                    myHandler.onDrawView(this,intent);
                }
//                isInputedText = false;
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
    public void operationStartActivity(Intent intent, String fromActivity, String fromApp) {
        Intent broadIntent = new Intent();
        broadIntent.putExtra(LocalActivityReceiver.TARGET_INTENT, intent);
        broadIntent.putExtra(LocalActivityReceiver.fromAppStart,fromApp);
        broadIntent.setAction(LocalActivityReceiver.openTargetActivityByIntent);
        Log.i("LZH","打开应用对应页面");
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

    /**
     * 检查页面是否被重复打开
     * @param intent
     * @return true 表示重复 ; false 表示是未在短时间内重复打开
     */
    private boolean isRepeat(Intent intent){
        String activityName = intent.getStringExtra(MonitorActivityService.RESUME_ACTIVITY_NAME);
        long preTime = 0,curTime = System.currentTimeMillis();
        if(hashMap.get(activityName)!=null){
            preTime = hashMap.get(activityName);
        }
        hashMap.put(activityName,curTime);
        Log.i("LZH","preTime: "+preTime+" curTime: "+curTime);
        if(curTime-preTime>1000){
            Log.i("LZH","显示："+activityName);
            return  false;
        }else {
            return true;
        }
    }

    private void setRecycle(Boolean b){
        recycle = b;
    }

    private static class OnResumeRunnable implements Runnable{
        private MyActivityHandler handler;
        private Operation operation;
        private Intent intent;
        private Boolean update = true;
        private MonitorActivityReceiver receiver;
        public OnResumeRunnable(MonitorActivityReceiver receiver,MyActivityHandler handler,Operation operation,Intent intent){
            this.handler = handler;
            this.operation = operation;
            this.intent = intent;
            this.receiver = receiver;
        }

        @Override
        public void run() {
            while(update){
                update = false;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("LZH","Intent sleep");
            }
            handler.onResumeActivity(operation,intent);
            receiver.setRecycle(true);
            Log.i("LZH","发出Intent");
        }
        public void setUpdate(Boolean b,Operation operation,Intent intent){
            update = b;
            this.operation = operation;
            this.intent = intent;
        }
    }

}
