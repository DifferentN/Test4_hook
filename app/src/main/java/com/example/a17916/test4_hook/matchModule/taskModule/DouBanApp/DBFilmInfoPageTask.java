package com.example.a17916.test4_hook.matchModule.taskModule.DouBanApp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.monitorService.MyActivityHandler;
import com.example.a17916.test4_hook.monitorService.OpenActivityTask;
import com.example.a17916.test4_hook.monitorService.Operation;

public class DBFilmInfoPageTask extends OpenActivityTask {
    private String curActivityName;
    private Intent tarIntent;
    private byte[] eventBytes;
    private boolean hasText = false;
    private int time = 0;
    private int preView = -1;
    public DBFilmInfoPageTask(Context context){
        super(null,context);

    }

    public DBFilmInfoPageTask(MyActivityHandler handler, Context context) {
        super(handler, context);
    }

    @Override
    public void onCreateActivity(Operation operation, Intent intent) {

    }

    @Override
    public void onResumeActivity(Operation operation, Intent intent) {
        if(!isAvailable()){
            Log.i("LZH","任务不可用");
            return ;
        }
        curActivityName = intent.getStringExtra(MonitorActivityService.RESUME_ACTIVITY_NAME);
        if(curActivityName.equals("com.douban.movie.activity.MainActivity")){
            tarIntent = savePreference.getIntent("com.douban.frodo.search.activity.SearchActivity");
            operation.operationStartActivity(tarIntent,curActivityName);
        }else if(curActivityName.equals("com.douban.frodo.search.activity.SearchActivity")){
            if(searchText!=null){
                operation.operationReplayInputEvent(searchText,curActivityName);
                hasText = true;
            }
            //以下可用
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            eventBytes = savePreference.readMotionEventsByte(curActivityName+"/MotionEvent");
//            operation.operationReplayMotionEvent(eventBytes,curActivityName);
//            myHandler.release(this);
        }
    }

    @Override
    public void onDestroyActivity(Operation operation, Intent intent) {

    }

    @Override
    public void onDrawView(Operation operation, Intent intent) {
        if(hasText&&curActivityName.equals("com.douban.frodo.search.activity.SearchActivity")){
            eventBytes = savePreference.readMotionEventsByte(curActivityName+"/MotionEvent");
            operation.operationReplayMotionEvent(eventBytes,curActivityName);
            myHandler.setFinishedTask(this);
        }
    }
}
