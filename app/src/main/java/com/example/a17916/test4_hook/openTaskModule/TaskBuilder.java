package com.example.a17916.test4_hook.openTaskModule;

import android.content.Context;

import com.example.a17916.test4_hook.monitorService.MyActivityHandler;
import com.example.a17916.test4_hook.share.SavePreference;

public abstract class TaskBuilder {
    protected Context context;
    protected SavePreference savePreference;
    protected MyActivityHandler handler;
    public TaskBuilder (Context context){
        this.context = context;
        savePreference = SavePreference.getInstance(context.getApplicationContext());
        handler = MyActivityHandler.getInstance();
    }
    public UnionOpenActivityTask generateTask(){
        UnionOpenActivityTask task = new UnionOpenActivityTask(handler);
        addStepToTask(task);
        return task;
    }
    public abstract  void addStepToTask(UnionOpenActivityTask task);
}
