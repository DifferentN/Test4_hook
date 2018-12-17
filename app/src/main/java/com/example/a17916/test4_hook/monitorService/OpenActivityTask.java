package com.example.a17916.test4_hook.monitorService;

public abstract class OpenActivityTask {
    private MyActivityHandler myHandler;
    public OpenActivityTask(MyActivityHandler handler){
        myHandler = handler;
    }
    public abstract void onCreateActivity(Operation operation);
    public abstract void onResumeActivity(Operation operation);
    public abstract void onDestroyActivity(Operation operation);
    protected void release(){
        myHandler.release(this);
    }
}
