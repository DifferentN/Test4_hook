package com.example.a17916.test4_hook.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;

import java.util.List;

public class ItemListener implements RecyclerViewItemListener {
    private List<Intent> list;
    private Context context;
    public ItemListener(List<Intent> list,Context context){
        this.list = list;
        this.context = context;
    }
    @Override
    public void onItemClick(View view, int pos) {
        //启动对应App

        launchIntent(list.get(pos));
    }
    private void launchIntent(Intent tarIntent){
        String packageName = tarIntent.getStringExtra(LocalActivityReceiver.targetPackageName);
        String activityName = tarIntent.getStringExtra(LocalActivityReceiver.targetActivityName);
        Intent intent  = context.getPackageManager().getLaunchIntentForPackage(packageName);

        Intent openActivity = new Intent();
        openActivity.setAction(MonitorActivityService.openByIntent);
        openActivity.putExtra(LocalActivityReceiver.targetPackageName,packageName);
        openActivity.putExtra(LocalActivityReceiver.targetActivityName,activityName);
        openActivity.putExtra("tarIntent",tarIntent);
        context.sendBroadcast(openActivity);

        context.startActivity(intent);
    }


}
