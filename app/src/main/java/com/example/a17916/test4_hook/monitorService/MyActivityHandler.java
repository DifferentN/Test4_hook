package com.example.a17916.test4_hook.monitorService;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class MyActivityHandler {
    public static MyActivityHandler myHandler;
    private ArrayList<OpenActivityTask> tasks;
    public static MyActivityHandler getInstance(){
        if(myHandler==null){
            myHandler = new MyActivityHandler();
        }
        return myHandler;
    }
    public MyActivityHandler(){
        tasks = new ArrayList<>();
    }

    public void onCreateActivity(Operation operation, Intent intent){

    }
    public void onResumeActivity(Operation operation,Intent intent){

    }
    public void onDestroyActivity(Operation operation,Intent intent){

    }
    public void release(OpenActivityTask task){
        OpenActivityTask temp;
        for(int i=0;i<tasks.size();i++){
            temp = tasks.get(i);
            if(temp==task){
                tasks.remove(i);
                return ;
            }
        }
        Log.i("LZH","不需移除，已不存在task");
    }
}
