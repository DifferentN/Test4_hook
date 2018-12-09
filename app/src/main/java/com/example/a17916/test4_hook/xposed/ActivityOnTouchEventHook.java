package com.example.a17916.test4_hook.xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.MotionEvent;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;

public class ActivityOnTouchEventHook extends XC_MethodHook {
    private HashMap<String ,Integer> hashMap = new HashMap<String,Integer>();
    private int time = 0;
    private String lastActivityName = "";
    private ArrayList<MotionEvent> list = new ArrayList<MotionEvent>();
    private String key = "";
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//        super.beforeHookedMethod(param);
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        boolean hasRun = (boolean) param.getResult();
        Activity activity = (Activity) param.thisObject;
        ComponentName componentName = activity.getComponentName();
        String activityName = componentName.getClassName();
//        Log.i("LZH","aName "+activityName);
        if (lastActivityName.equals("")) {
            lastActivityName = componentName.getClassName();
        }else if(!lastActivityName.equals(componentName.getClassName())){
//            Log.i("LZH","send save MotionEvent");
            key = lastActivityName+"/MotionEvent";
            Bundle bundle = new Bundle();
            Parcel parcel = writeMotionEvents(list);
//            byte [] bytes = parcel.marshall();
//            testRead(bytes);
            if(parcel==null){
                return;
            }
            bundle.putByteArray("MotionEvents",parcel.marshall());
            bundle.putString("eventKey",key);
            Intent intent = new Intent();
            intent.putExtra("MotionEvent",bundle);
            intent.setAction(MonitorActivityService.saveMotionEvent);
            activity.sendBroadcast(intent);
            lastActivityName = componentName.getClassName();
            list.clear();
        }

        MotionEvent motionEvent;
        if(hasRun){
//            Log.i("LZH","receive activity "+componentName.getClassName());
            motionEvent = MotionEvent.obtain((MotionEvent) param.args[0]);
            list.add(motionEvent);
//            Log.i("LZH","event: X: "+motionEvent.getRawX()+" y: "+motionEvent.getRawY());
        }
    }

    public Parcel writeMotionEvents(List<MotionEvent> list){
        for(MotionEvent event:list){
//            Log.i("LZH","save event x: "+event.getRawX()+" y: "+event.getRawY());
        }
        Parcel parcel = null;
        if(list.size()<=0){
            Log.i("LZH","list size is 0");
            return null;
        }
        parcel = Parcel.obtain();
        parcel.writeInt(list.size());
        parcel.writeTypedList(list);
        return parcel;
    }
}
