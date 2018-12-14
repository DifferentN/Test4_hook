package com.example.a17916.test4_hook.matchModule.impl;

import android.content.Context;
import android.content.Intent;

import com.example.a17916.test4_hook.matchModule.IntentAdapter;
import com.example.a17916.test4_hook.share.SavePreference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class YiDaoUseCar extends IntentAdapter {

    @Override
    public List<Intent> getIntentsByJson(String tag, JSONObject jsonObject) {
        List<Intent> intents = new ArrayList<>();
        intents.add(test1GetIntent(tag));
//        intents.add(test1GetIntent(tag));
        return intents;
    }

    @Override
    public List<Intent> getIntentByValueNode(String tag, Object o) {
        return null;
    }

    @Override
    public JSONObject transform(JSONObject jsonObject) {
        return null;
    }
    //获得事先保存的Intent
    private Intent test1GetIntent(String tag){
        //com.yongche.android.YDBiz.Order.HomePage.MainActivity
        //com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity
        String activityName = "com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity";
        SavePreference savePreference = SavePreference.getInstance(context);
        Intent intent = savePreference.getIntent(activityName);
        return intent;
    }
}
