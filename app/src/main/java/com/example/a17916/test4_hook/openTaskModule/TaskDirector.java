package com.example.a17916.test4_hook.openTaskModule;

import android.util.Log;

public class TaskDirector {
    private TaskBuilder builder;

    public void setBuilder(TaskBuilder builder) {
        this.builder = builder;
    }
    public UnionOpenActivityTask constructTask(){
        if(builder==null){
            Log.e("LZH","构造者为null");
            return null;
        }
        return builder.generateTask();
    }
}
