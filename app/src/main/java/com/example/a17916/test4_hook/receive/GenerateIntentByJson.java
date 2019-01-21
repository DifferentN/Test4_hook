package com.example.a17916.test4_hook.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GenerateIntentByJson extends BroadcastReceiver {
    public static final String GENERATE_INTENT_BY_JSON = "GENERATE_INTENT_BY_JSON";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

    }
}
