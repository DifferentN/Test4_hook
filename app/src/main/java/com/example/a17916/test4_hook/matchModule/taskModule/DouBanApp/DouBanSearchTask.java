package com.example.a17916.test4_hook.matchModule.taskModule.DouBanApp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.monitorService.MyActivityHandler;
import com.example.a17916.test4_hook.monitorService.OpenActivityTask;
import com.example.a17916.test4_hook.monitorService.Operation;

public class DouBanSearchTask extends OpenActivityTask {
    private String curActivityName;
    private Intent tarIntent;
    public DouBanSearchTask(MyActivityHandler handler, Context context) {
        super(handler, context);
    }

    @Override
    public void onCreateActivity(Operation operation, Intent intent) {

    }

    @Override
    public void onResumeActivity(Operation operation, Intent intent) {
        curActivityName = intent.getStringExtra(MonitorActivityService.RESUME_ACTIVITY_NAME);
        if(curActivityName.equals("com.douban.movie.activity.MainActivity")){
            tarIntent = savePreference.getIntent("com.douban.frodo.search.activity.SearchActivity");
            if(tarIntent==null){
                Log.i("LZH","无法获得到达搜索的Intent");
            }
            operation.operationStartActivity(tarIntent,curActivityName);
            myHandler.setFinishedTask(this);
        }
    }

    @Override
    public void onDestroyActivity(Operation operation, Intent intent) {

    }

    @Override
    public void onDrawView(Operation operation, Intent intent) {

    }
}
