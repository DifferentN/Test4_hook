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
    private String curAppName;
    private String requireActivityName;

    private int time = 5;

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

        curStep = steps.get(0);
        curActivityName = intent.getStringExtra(MonitorActivityService.RESUME_ACTIVITY_NAME);
        curAppName = intent.getStringExtra(MonitorActivityService.RESUME_APP_NAME);
        requireActivityName = curStep.getActivityName();
        Log.i("LZH","requ: "+requireActivityName+" cur: "+curActivityName);
        Log.i("LZH","step type: "+curStep.getStepType());
//        if(curStep.getStepType()==StepContent.MOTION_EVENT_TYPE){
//            //不能在打开页面时，播放点击事件
//            return;
//        }
        Log.i("LZH","curAppName: "+curAppName+" targetAppName: "+curStep.getAppName());
        if(curStep.getStepType()!=StepContent.INTENT_TYPE&&!curActivityName.equals(requireActivityName)){
            return;
        }else if(curStep.getStepType()==StepContent.INTENT_TYPE&&!curAppName.equals(curStep.getAppName())){
            return;
        }
//        if(curStep.getStepType()==StepContent.INTENT_TYPE&&!requireActivityName.equals(curActivityName)){
//            return;
//        }
        executeStep(curStep,operation);
        steps.remove(0);

        if(!isStepsAvailable()){
            myHandler.setFinishedTask(this);
        }
    }

    @Override
    public void onDestroyActivity(Operation operation, Intent intent) {
//        if(!isStepsAvailable()){
//            Log.i("LZH","出错，无法取得任务的下一步");
//            myHandler.setFinishedTask(this);
//        }
////        curStep = steps.get(0);
////        curActivityName = intent.getStringExtra(MonitorActivityService.CREATE_ACTIVITY_NAME);
////        if(curStep.getStepCondition().canExecute(curActivityName)){
////            executeStep(curStep,operation);
////            steps.remove(0);
////        }
//
//        curStep = steps.get(0);
//        curActivityName = intent.getStringExtra(MonitorActivityService.DESTROY_ACTIVITY_NAME);
//        requireActivityName = curStep.getActivityName();
//        if(!curActivityName.equals(requireActivityName)){
//            return;
//        }
//
//        executeStep(curStep,operation);
//        steps.remove(0);
//
//        if(!isStepsAvailable()){
//            myHandler.setFinishedTask(this);
//        }
    }

    @Override
    public void onDrawView(Operation operation, Intent intent) {
        if(!isStepsAvailable()){
            Log.i("LZH","出错，无法取得任务的下一步");
            myHandler.setFinishedTask(this);
        }

        Log.i("LZH","time: "+time);
        if(--time>0){
            //多次发送绘制完成广播，保证显示出搜索结果
            return;
        }
        curStep = steps.get(0);
//        curActivityName = intent.getStringExtra(MonitorActivityService.CREATE_ACTIVITY_NAME);
        requireActivityName = curStep.getActivityName();
//        if(!curActivityName.equals(requireActivityName)){
//            return;
//        }

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
                //在未打开对应应用的情况下，使用此方法打开对应的Activity
//                operation.operationStartActivity(step.getSendIntent(),requireActivityName);
                //在已经打开App的情况下，使用此方法打开对应的Activity
                operation.operationStartActivity(step.getSendIntent(),null,
                        step.getAppName());
                break;
            case StepContent.INPUT_TEXT_TYPE:
                operation.operationReplayInputEvent(step.getInputText(),requireActivityName);
                break;
            case StepContent.MOTION_EVENT_TYPE:
                operation.operationReplayMotionEvent(step.getEventBytes(),requireActivityName);
                break;
        }
    }
    public static class EventRunnable implements Runnable{

        private Operation operation;
        private StepContent step;
        private String requireActivityName;
        public EventRunnable(StepContent step,Operation operation,String requireActivityName){
            this.operation = operation;
            this.requireActivityName = requireActivityName;
            this.step = step;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            operation.operationReplayMotionEvent(step.getEventBytes(),requireActivityName);
        }
    }
}
