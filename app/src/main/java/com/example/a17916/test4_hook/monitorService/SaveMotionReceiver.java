package com.example.a17916.test4_hook.monitorService;

import android.content.BroadcastReceiver;
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
                if(prepareSaveEvent){
                    Log.i("LZH","save MotionEvent");
                    tempSaveMotionEvent(intent);
                }

//                Bundle bundle = intent.getBundleExtra("MotionEvent");
//                motionEventKey = bundle.getString("eventKey");
//                bytes = bundle.getByteArray("MotionEvents");
//                saveMotionEvent(motionEventKey,bytes);
            case MonitorActivityService.SAVE_EDIT_TEXT:
                prepareSaveEvent = true;
                editText = intent.getStringExtra(MonitorActivityService.EDIT_TEXT);
                Log.i("LZH","editText: "+editText);
                break;
            case MonitorActivityService.ON_CREATE_STATE:
                saveMotionEvent(saveActivity,editText);
//                Log.i("LZH","save at create");
                break;
            case MonitorActivityService.ON_RESUME_STATE:
//                Log.i("LZH","save at resume");
                saveMotionEvent(saveActivity,editText);
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

//    private void saveMotionEvent(String motionEventKey, byte[] bytes) {
//        preference.writeMotionEvent(motionEventKey,bytes);
//    }

    private void tempSaveMotionEvent(Intent intent){
        byte[] bytes = intent.getByteArrayExtra(MonitorActivityService.MOTION_EVENT);
        saveActivity = intent.getStringExtra(MonitorActivityService.EVENT_ACTIVITY);
        MotionEvent event = transformToMotionEvent(bytes);
        preEvents.add(event);
    }
    //用类名和输入的搜索词 作为key值
    private void saveMotionEvent(String activityName,String text){
        prepareSaveEvent = false;
//        Log.i("LZH","preEvents size: "+preEvents.size()+" activityName: "+activityName+" text: "+text);
        if(preEvents.isEmpty()&&activityName!=null&&activityName.length()>0&&text!=null){
            return ;
        }
        String key = activityName+"/"+text;
        Parcel parcel = writeMotionEvents(preEvents);
        if(parcel==null){
            Log.i("LZH","保存点击事件失败");
            return ;
        }
        bytes = parcel.marshall();
        Log.i("LZH","保存点击事件，key: "+key);
        preference.writeMotionEvent(key,bytes);
        reset();
    }
    private MotionEvent transformToMotionEvent(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);
        MotionEvent event = parcel.readParcelable(MotionEvent.class.getClassLoader());
        return event;
    }

    private Parcel writeMotionEvents(List<MotionEvent> list){
//        for(MotionEvent event:list){
////            Log.i("LZH","save event x: "+event.getRawX()+" y: "+event.getRawY());
//        }
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
