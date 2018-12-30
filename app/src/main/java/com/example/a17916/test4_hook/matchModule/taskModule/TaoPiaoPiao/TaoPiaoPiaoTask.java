package com.example.a17916.test4_hook.matchModule.taskModule.TaoPiaoPiao;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.monitorService.MyActivityHandler;
import com.example.a17916.test4_hook.monitorService.OpenActivityTask;
import com.example.a17916.test4_hook.monitorService.Operation;
import com.example.a17916.test4_hook.share.SavePreference;

public class TaoPiaoPiaoTask extends OpenActivityTask {
    private String curActivity;
    private int time=0;
    public TaoPiaoPiaoTask(MyActivityHandler handler,Context context) {
        super(handler,context);
    }

    @Override
    public void onCreateActivity(Operation operation, Intent intent) {
        curActivity = intent.getStringExtra(MonitorActivityService.CREATE_ACTIVITY_NAME);
    }

    @Override
    public void onResumeActivity(Operation operation,Intent intent) {
        curActivity = intent.getStringExtra(MonitorActivityService.RESUME_ACTIVITY_NAME);
        Intent tarIntent = savePreference.getIntent("com.taobao.movie.android.app.oscar.ui.cinema.activity.ScheduleListRootActivity");
        if(curActivity.equals("com.taobao.movie.android.app.home.activity.MainActivity")){
            time++;
            if(time<2){
                return ;
            }
            Log.i("LZH","打开目标Activity");
            operation.operationStartActivity(tarIntent,curActivity);
        }
        myHandler.setFinishedTask(this);
    }
    @Override
    public void onDestroyActivity(Operation operation,Intent intent) {

    }

    @Override
    public void onDrawView(Operation operation, Intent intent) {

    }
}
