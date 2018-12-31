package com.example.a17916.test4_hook.openTaskModule;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.monitorService.MyActivityHandler;
import com.example.a17916.test4_hook.monitorService.OpenActivityTask;
import com.example.a17916.test4_hook.monitorService.Operation;

import java.util.ArrayList;

public class UnionOpenActivityTask extends OpenActivityTask {
    private int sumStepNum;
    private ArrayList<StepContent> steps;
    private StepContent curStep;
    private String curActivityName;

    public UnionOpenActivityTask(MyActivityHandler handler){
        super(handler,null);
        steps = new ArrayList<>();
    }

    /**
     *
     * @param handler
     * @param context 不是必须的，因为不再用其获得保存点击事件
     */
    public UnionOpenActivityTask(MyActivityHandler handler, Context context) {
        super(handler, context);
    }

    @Override
    public void onCreateActivity(Operation operation, Intent intent) {
//        if(!isStepsAvailable()){
//            Log.i("LZH","出错，无法取得任务的下一步");
//            myHandler.setFinishedTask(this);
//        }
//        curStep = steps.get(0);
//        curActivityName = intent.getStringExtra(MonitorActivityService.CREATE_ACTIVITY_NAME);
//        if(curStep.getStepCondition().canExecute(curActivityName)){
//            executeStep(curStep,operation);
//            steps.remove(0);
//        }
//        if(!isStepsAvailable()){
//            myHandler.setFinishedTask(this);
//        }
    }

    //以下骤的背景：事先打开应用，收到命令立刻执行步骤
    //
    //
    //
    //
    //
    @Override
    public void onResumeActivity(Operation operation, Intent intent) {
        if(!isStepsAvailable()){
            Log.i("LZH","出错，无法取得任务的下一步");
            myHandler.setFinishedTask(this);
        }
//        curStep = steps.get(0);
//        curActivityName = intent.getStringExtra(MonitorActivityService.CREATE_ACTIVITY_NAME);
//        if(curStep.getStepCondition().canExecute(curActivityName)){
//            executeStep(curStep,operation);
//            steps.remove(0);
//        }

        curStep = steps.get(0);
        curActivityName = curStep.getActivityName();
        executeStep(curStep,operation);
        steps.remove(0);

        if(!isStepsAvailable()){
            myHandler.setFinishedTask(this);
        }
    }

    @Override
    public void onDestroyActivity(Operation operation, Intent intent) {
        if(!isStepsAvailable()){
            Log.i("LZH","出错，无法取得任务的下一步");
            myHandler.setFinishedTask(this);
        }
//        curStep = steps.get(0);
//        curActivityName = intent.getStringExtra(MonitorActivityService.CREATE_ACTIVITY_NAME);
//        if(curStep.getStepCondition().canExecute(curActivityName)){
//            executeStep(curStep,operation);
//            steps.remove(0);
//        }

        curStep = steps.get(0);
        curActivityName = curStep.getActivityName();
        executeStep(curStep,operation);
        steps.remove(0);

        if(!isStepsAvailable()){
            myHandler.setFinishedTask(this);
        }
    }

    @Override
    public void onDrawView(Operation operation, Intent intent) {
        if(!isStepsAvailable()){
            Log.i("LZH","出错，无法取得任务的下一步");
            myHandler.setFinishedTask(this);
        }
//        curStep = steps.get(0);
//        curActivityName = intent.getStringExtra(MonitorActivityService.CREATE_ACTIVITY_NAME);
//        if(curStep.getStepCondition().canExecute(curActivityName)){
//            executeStep(curStep,operation);
//            steps.remove(0);
//        }
        curStep = steps.get(0);
        curActivityName = curStep.getActivityName();
        executeStep(curStep,operation);
        steps.remove(0);

        if(!isStepsAvailable()){
            myHandler.setFinishedTask(this);
        }
    }
    public void addStep(StepContent oneStep){
        steps.add(oneStep);
        sumStepNum++;
    }
    public boolean isStepsAvailable(){
        if(steps.isEmpty()||steps.size()<=0){
            return false;
        }
        return true;
    }
    private void executeStep(StepContent step,Operation operation){
        switch (step.getStepType()){
            case StepContent.INTENT_TYPE:
                operation.operationStartActivity(step.getSendIntent(),curActivityName);
                break;
            case StepContent.INPUT_TEXT_TYPE:
                operation.operationReplayInputEvent(step.getInputText(),curActivityName);
                break;
            case StepContent.MOTION_EVENT_TYPE:
                //延时1s保证点击事件的播放
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                operation.operationReplayMotionEvent(step.getEventBytes(),curActivityName);
                break;
        }
    }
}
