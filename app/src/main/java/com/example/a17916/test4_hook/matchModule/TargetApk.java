package com.example.a17916.test4_hook.matchModule;

import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TargetApk {
    private ArrayMap<String ,IntentAdapter> tag2adapter;

    public TargetApk(){
        tag2adapter = new ArrayMap<>();
    }
    public List<Intent> getIntentsByTag(String tag, JSONObject jsonObject){
        IntentAdapter intentAdapter = tag2adapter.get(tag);
        List<Intent> list = intentAdapter.getIntentsByJson(tag,jsonObject);
        return list;
    }

    public void setTag2adapter(String tag,IntentAdapter intentAdapter){
        tag2adapter.put(tag,intentAdapter);
    }
}
