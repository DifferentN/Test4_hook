package com.example.a17916.test4_hook.matchModule;

import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import java.util.List;

public abstract class IntentAdapter {

    protected Context context;

    public IntentAdapter(){}

    public IntentAdapter(Context context){
        this.context = context;
    }

    public void setContext(Context context){
        this.context = context;
    }
    public abstract List<Intent> getIntentsByJson(String tag, JSONObject jsonObject);

    public abstract List<Intent> getIntentByValueNode(String tag,Object o);

    public abstract JSONObject transform(JSONObject jsonObject);

}
