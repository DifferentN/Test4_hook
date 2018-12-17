package com.example.a17916.test4_hook.monitorService;

import android.content.Intent;
import android.view.MotionEvent;

import java.util.List;

public interface Operation {
    void operationStartActivity(Intent intent);
    void operationReplayInputEvent(String text);
    void operationReplayMotionEvent(byte[] events );
}
