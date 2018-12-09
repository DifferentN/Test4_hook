package com.example.a17916.test4_hook.matchModule;

import android.content.Context;
import android.content.Intent;

import com.example.a17916.test4_hook.share.SavePreference;

public class MotionManager {
    private Context context;
    public MotionManager(Context context){
        this.context = context;
    }

    //测试中 获得到达某页面后的点击事件
    public byte[] obtainFromPreference(String activityName,String tag){
        byte[] res = null;
        SavePreference savePreference = SavePreference.getInstance(context);
        String key = generateKey(activityName,tag);
        res = savePreference.readMotionEventsByte(key);
        return res;
    }

    private String generateKey(String activityName, String tag) {
        String key = activityName+"/"+tag;
        return key;
    }
}
