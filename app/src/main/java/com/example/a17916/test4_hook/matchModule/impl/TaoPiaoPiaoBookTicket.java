package com.example.a17916.test4_hook.matchModule.impl;

import android.content.Intent;
import android.util.Log;

import com.example.a17916.test4_hook.matchModule.IntentAdapter;
import com.example.a17916.test4_hook.share.SavePreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaoPiaoPiaoBookTicket extends IntentAdapter {
    @Override
    public List<Intent> getIntentsByJson(String tag, JSONObject jsonObject) {
        ArrayList<Intent> list = new ArrayList<>();
        SavePreference savePreference = SavePreference.getInstance(context);
        Intent intent = savePreference.getIntent("com.taobao.movie.android.app.oscar.ui.cinema.activity.ScheduleListRootActivity");
        list.add(intent);
        if(intent==null){
            Log.i("LZH","无法获得淘票票的Intent");
        }
        return list;
    }

    @Override
    public List<Intent> getIntentByValueNode(String tag, Object o) {
        return null;
    }

    @Override
    public JSONObject transform(JSONObject jsonObject) {
        return null;
    }
}
