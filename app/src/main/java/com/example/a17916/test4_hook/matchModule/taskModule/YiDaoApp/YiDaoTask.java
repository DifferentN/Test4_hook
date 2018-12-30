package com.example.a17916.test4_hook.matchModule.taskModule.YiDaoApp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.monitorService.MyActivityHandler;
import com.example.a17916.test4_hook.monitorService.OpenActivityTask;
import com.example.a17916.test4_hook.monitorService.Operation;

public class YiDaoTask extends OpenActivityTask {
    private String curActivity;
    private byte eventBytes[];
    private boolean hasText = false;
    public YiDaoTask(MyActivityHandler handler, Context context) {
        super(handler, context);
    }

    @Override
    public void onCreateActivity(Operation operation, Intent intent) {

    }

    @Override
    public void onResumeActivity(Operation operation, Intent intent) {
        curActivity = intent.getStringExtra(MonitorActivityService.RESUME_ACTIVITY_NAME);
        if(curActivity.equals("com.yongche.android.YDBiz.Order.HomePage.MainActivity")){
            //获得首页的点击事件
            Log.i("LZH","播放首页点击事件");
            eventBytes = savePreference.readMotionEventsByte(curActivity+"/MotionEvent");
            operation.operationReplayMotionEvent(eventBytes,curActivity);
        }else if (curActivity.equals("com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity")){
            //发送要输入的搜索词
            operation.operationReplayInputEvent(searchText,curActivity);
            hasText = true;
        }
    }

    @Override
    public void onDestroyActivity(Operation operation, Intent intent) {

    }

    @Override
    public void onDrawView(Operation operation, Intent intent) {
        if(hasText&&curActivity.equals("com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity")){
            //获得在输入目的地页面的点击事件
            eventBytes = savePreference.readMotionEventsByte(curActivity+"/MotionEvent");
            operation.operationReplayMotionEvent(eventBytes,curActivity);
        }
        myHandler.setFinishedTask(this);

    }
}
