package com.example.a17916.test4_hook.manageActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;

public class ControllerReciver extends BroadcastReceiver {
    private MonitorActivityService.TransportBinder transportBinder = null;
    private MonitorActivityService monitorService;
    private String packageName = "";
    private Intent tarIntent;
    public ControllerReciver(MonitorActivityService.TransportBinder transportBinder){
        this.transportBinder = transportBinder;
        monitorService = transportBinder.getService();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case ActivityController.SEND_ACTIVITY_INTENT:
                packageName = intent.getStringExtra(ActivityController.PK_NAME);
                tarIntent = intent.getParcelableExtra(ActivityController.TARGET_INTENT);
                monitorService.openActivity(packageName,tarIntent);
                break;
            case ActivityController.SEND_INTENT_MOTION:
                break;
        }
    }
}
