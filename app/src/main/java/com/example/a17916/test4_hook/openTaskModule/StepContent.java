package com.example.a17916.test4_hook.openTaskModule;

import android.content.Intent;

public class StepContent {
    public final static int INTENT_TYPE = 0x01; //说明这一步是发送Intent的操作
    public final static int INPUT_TEXT_TYPE = 0x02; //说明这一步是发送搜索字的操作
    public final static int MOTION_EVENT_TYPE = 0x04; //说明这一步是发送点击事件的操作
    private int stepType;
    private Intent sendIntent;
    private String inputText;
    private byte[] eventBytes;
    private StepCondition stepCondition;

    public StepContent(int type, Intent intent, String text, byte[] bytes){
        stepType = type;
        sendIntent = intent;
        eventBytes = bytes;
    }

    public void setStepCondition(StepCondition stepCondition){
        this.stepCondition = stepCondition;
    }

    public int getStepType() {
        return stepType;
    }

    public Intent getSendIntent() {
        return sendIntent;
    }

    public String getInputText() {
        return inputText;
    }

    public byte[] getEventBytes() {
        return eventBytes;
    }

    public StepCondition getStepCondition() {
        return stepCondition;
    }
}
