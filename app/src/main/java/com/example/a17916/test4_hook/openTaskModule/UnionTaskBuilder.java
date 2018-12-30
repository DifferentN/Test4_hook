package com.example.a17916.test4_hook.openTaskModule;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class UnionTaskBuilder extends TaskBuilder {
    private UnionOpenActivityTask task;
    private List<StepContent> steps;
    public UnionTaskBuilder(Context context) {
        super(context);
        steps = new ArrayList<>();
    }

    @Override
    public void addStepToTask(UnionOpenActivityTask task) {

    }

    public void addStep(StepContent step){
        steps.add(step);
    }

    public void addIntentStep(Intent intent){
        StepContent stepContent = new StepContent(StepContent.INTENT_TYPE,intent,null,null);
        steps.add(stepContent);
    }

    public void addTextStep(String text){
        StepContent stepContent = new StepContent(StepContent.INPUT_TEXT_TYPE,null,text,null);
        steps.add(stepContent);
    }

    public void addMotionEventStep(byte[] bytes){
        StepContent stepContent = new StepContent(StepContent.MOTION_EVENT_TYPE,null,null,bytes);
        steps.add(stepContent);
    }

    @Override
    public UnionOpenActivityTask generateTask() {
        task = new UnionOpenActivityTask(handler);
        int size = steps.size();
        for(int i=0;i<size;i++){
            task.addStep(steps.remove(0));
        }
        return task;
    }
}
