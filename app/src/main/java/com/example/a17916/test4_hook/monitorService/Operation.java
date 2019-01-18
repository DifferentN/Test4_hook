package com.example.a17916.test4_hook.monitorService;

import android.content.Intent;
import android.view.MotionEvent;

import java.util.List;

/**
 * 将操作发送给LocalActivityReceiver 执行
 */
public interface Operation {

    /**
     *
     * @param intent
     * @param fromActivity 从哪一个activity打开目标页面（用于未打开应用的情况）
     */
    void operationStartActivity(Intent intent,String fromActivity);

    /**
     *
     * @param intent
     * @param fromActivity
     * @param fromApp 从哪一个App打开目标页面（用于已经打开应用的情况）
     */
    void operationStartActivity(Intent intent,String fromActivity,String fromApp);
    void operationReplayInputEvent(String text,String fromActivity);
    void operationReplayMotionEvent(byte[] events,String fromActivity);
}
