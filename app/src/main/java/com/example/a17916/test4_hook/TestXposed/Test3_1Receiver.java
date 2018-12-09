package com.example.a17916.test4_hook.TestXposed;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class Test3_1Receiver extends BroadcastReceiver {
    private Handler sMainThreadHandler;
    private Activity activity;
    public Test3_1Receiver(Handler handler,Activity activity){
        sMainThreadHandler = handler;
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent startSecond = new Intent();
                ComponentName componentName = new ComponentName("com.example.a17916.test3_1","com.example.a17916.test3_1.SecondActivity");
                startSecond.setComponent(componentName);
                activity.startActivity(startSecond);
            }
        };
        sMainThreadHandler.post(runnable);
    }
}
