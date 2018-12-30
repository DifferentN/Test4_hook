package com.example.a17916.test4_hook.monitorService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.MotionEvent;

import com.example.a17916.test4_hook.share.SavePreference;

import java.util.ArrayList;
import java.util.List;

public class SaveMotionReceiver extends BroadcastReceiver {
    private Context context;
    private byte[] bytes;
    private SavePreference preference;
    private ArrayList<MotionEvent> preEvents;
    private String editText;
    private String saveActivity;
    private boolean prepareSaveEvent = false;

    public SaveMotionReceiver(Context context){
        this.context = context;
        preference = SavePreference.getInstance(context.getApplicationContext());
        preEvents = new ArrayList<>();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case MonitorActivityService.SAVE_INTENT:
                saveIntent(intent);
                break;
            case MonitorActivityService.saveIntentInfo:
                break;
            case MonitorActivityService.SAVE_MOTION_EVENT:
                tempSaveMotionEvent(intent);
            case MonitorActivityService.SAVE_EDIT_TEXT:
                prepareSaveEvent = true;
                editText = intent.getStringExtra(MonitorActivityService.EDIT_TEXT);
                break;
            case MonitorActivityService.ON_CREATE_STATE:
                break;
            case MonitorActivityService.ON_RESUME_STATE:
                saveMotionEvent();
                saveActivity = intent.getStringExtra(MonitorActivityService.RESUME_ACTIVITY_NAME);
                break;
        }
    }

    private void saveIntent(Intent intent) {
        Intent opendIntent = intent.getParcelableExtra("opendActivityIntent");
        String openActivityName = intent.getStringExtra("openActivityName");
        String key = openActivityName;
        if(opendIntent==null){
            Log.i("LZH",openActivityName+" intent is null ");
        }

        preference.writeIntent(key,opendIntent);
    }

    private void tempSaveMotionEvent(Intent intent){
        byte[] bytes = intent.getByteArrayExtra(MonitorActivityService.MOTION_EVENT);
        saveActivity = intent.getStringExtra(MonitorActivityService.EVENT_ACTIVITY);
        MotionEvent event = transformToMotionEvent(bytes);
        preEvents.add(event);
    }

    /**
     * 保存页面的点击事件，以activityName作为点击事件的key值
     * 保存规则：记录当前页面的点击事件，当页面发生跳转时，将点击事件转化为二进制保存
     */
    private void saveMotionEvent(){
        if(saveActivity==null||preEvents.size()<=0){
//            Log.i("LZH","无法保存点击事件");
            return ;
        }

        Parcel parcel = writeMotionEvents(preEvents);
        if(parcel==null){
            Log.i("LZH","保存点击事件失败");
            return ;
        }
        bytes = parcel.marshall();
        preference.writeMotionEvent(saveActivity+"/MotionEvent",bytes);
        preEvents.clear();
    }
    private MotionEvent transformToMotionEvent(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);
        MotionEvent event = parcel.readParcelable(MotionEvent.class.getClassLoader());
        return event;
    }

    private Parcel writeMotionEvents(List<MotionEvent> list){
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
    private void reset(){
        preEvents.clear();
        editText = null;
        prepareSaveEvent = false;
    }
}
