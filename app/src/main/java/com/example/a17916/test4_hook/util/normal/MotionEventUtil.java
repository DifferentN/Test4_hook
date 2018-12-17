package com.example.a17916.test4_hook.util.normal;

import android.os.Parcel;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

public class MotionEventUtil {

    public static MotionEvent[] tranformtoMotionEvent(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);
        int size = parcel.readInt();
        MotionEvent[] motionEvents = new MotionEvent[size];
        parcel.readTypedArray(motionEvents,MotionEvent.CREATOR);
        return motionEvents;

    }
    public static Parcel writeMotionEvents(List<MotionEvent> list){
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
