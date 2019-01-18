package com.example.a17916.test4_hook.activity.showResult;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.a17916.test4_hook.manageActivity.ActivityController;

import com.example.a17916.test4_hook.monitorService.OpenActivityTask;
import com.example.a17916.test4_hook.openTaskModule.TaskGenerator;

import java.util.List;

public class ItemListener implements RecyclerViewItemListener {
    private List<ShowItem> list;
    private Context context;
    private ActivityController controller;
    private TaskGenerator taskGenerator;
    private Activity activity;
    public ItemListener(List<ShowItem> list,Context context){
        this.list = list;
        this.context = context;
        controller = ActivityController.getInstance(context);
        taskGenerator = new TaskGenerator(context);//转为getInstance
        activity = (Activity) context;
    }
    @Override
    public void onItemClick(View view, int pos) {
        //启动对应App

        launchIntent(list.get(pos));
    }
    private void launchIntent(ShowItem item){
//        Intent tarIntent = item.getIntent();
//        ComponentName componentName = tarIntent.getComponent();
//        String packageName = componentName.getPackageName();
//        String activityName = componentName.getClassName();
//        Intent intent  = context.getPackageManager().getLaunchIntentForPackage(packageName);

        ActivityController controller = ActivityController.getInstance(context);
        OpenActivityTask task = taskGenerator.generatorTask(item.getActivityName(),item.getResourceType(),
                item.getResEntityName(),item.getResourceId());
        controller.addTask(item.getPkName(),task);

//        activity.finish();

    }



}
