package com.example.a17916.test4_hook.monitorService;

import android.content.Intent;
import android.view.MotionEvent;

import java.util.List;

/**
 * 将操作发送给LocalActivityReceiver 执行
 */
public interface Operation {

    void operationStartActivity(Intent intent,String fromActivity);
    void operationReplayInputEvent(String text,String fromActivity);
    void operationReplayMotionEvent(byte[] events,String fromActivity);
}
