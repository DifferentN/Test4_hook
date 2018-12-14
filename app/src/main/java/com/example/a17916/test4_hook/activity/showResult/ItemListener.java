package com.example.a17916.test4_hook.activity.showResult;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.a17916.test4_hook.activity.showResult.RecyclerViewItemListener;
import com.example.a17916.test4_hook.activity.showResult.ShowItem;
import com.example.a17916.test4_hook.manageActivity.ActivityController;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;

import java.util.List;

public class ItemListener implements RecyclerViewItemListener {
    private List<ShowItem> list;
    private Context context;
    public ItemListener(List<ShowItem> list,Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public void onItemClick(View view, int pos) {
        //启动对应App

        launchIntent(list.get(pos));
    }
    private void launchIntent(ShowItem item){
        Intent tarIntent = item.getIntent();
        ComponentName componentName = tarIntent.getComponent();
        String packageName = componentName.getPackageName();
        String activityName = componentName.getClassName();
//        Intent intent  = context.getPackageManager().getLaunchIntentForPackage(packageName);

        ActivityController controller = ActivityController.getInstance(context);
        controller.openActivity(packageName,tarIntent);
//        Intent openActivity = new Intent();
//        openActivity.setAction(MonitorActivityService.openByIntent);
//        openActivity.putExtra(LocalActivityReceiver.targetPackageName,packageName);
//        openActivity.putExtra(LocalActivityReceiver.targetActivityName,activityName);
//        openActivity.putExtra("tarIntent",tarIntent);
//        context.sendBroadcast(openActivity);
//
//        context.startActivity(intent);
    }


}