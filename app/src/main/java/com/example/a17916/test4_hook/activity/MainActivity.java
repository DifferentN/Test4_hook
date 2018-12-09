package com.example.a17916.test4_hook.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.a17916.test4_hook.R;
import com.example.a17916.test4_hook.ViewManager.FloatViewManager;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.share.SavePreference;

public class MainActivity extends AppCompatActivity {

    private String packageName = "";
    private String activityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartSendDataService();

    }

    public void startFloatBt(View view){

        FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
        floatViewManager.showFloatButton();
    }
    private void StartSendDataService(){
        Intent intent = new Intent(this,MonitorActivityService.class);
        startService(intent);
    }

    public void openDBMovie(View view){
        PackageManager packageManager = getPackageManager();

        Intent intent = new Intent();
//        com.douban.movie
        intent = packageManager.getLaunchIntentForPackage("com.douban.movie");
        startActivity(intent);

//        Intent broadcast = new Intent();
//        broadcast.setAction(LocalActivityReceiver.openTargetActivityByIntentInfo);
//        Bundle bundle = new Bundle();
//        bundle.putString("packageName","com.example.a17916.test3_1");//com.douban.movie
////        com.douban.frodo.subject.activity.LegacySubjectActivity
//        bundle.putString("tarActivityName","com.example.a17916.test3_1.SecondActivity");
//        bundle.putString("dataUri","douban://douban.com/movie/3168101");
//        broadcast.putExtras(bundle);
//        sendBroadcast(broadcast);
    }
    public void openByIntent(View view){
//        Intent openActivity = new Intent();
//        openActivity.setAction(OpenActivityService.openByIntent);
//        openActivity.putExtra(OpenActivityService.targetPackageName,packageName);
//        openActivity.putExtra(OpenActivityService.targetActivityName,activityName);
//        sendBroadcast(openActivity);
//
//        PackageManager packageManager = getPackageManager();
//        Intent intent = new Intent();
////        com.douban.movie
//        intent = packageManager.getLaunchIntentForPackage("com.yongche.android");
//        startActivity(intent);
        openCreateIntent();
    }
    public void openCreateIntent(){
        int time = 0;
//        packageName = "com.handsgo.jiakao.android";
//        activityName = "com.handsgo.jiakao.android.CommonList";
        packageName = "com.handsgo.jiakao.android";
        activityName = "com.handsgo.jiakao.android.MyWebView";
        Intent tarIntent = new Intent();
        Intent openActivity = new Intent();
        ComponentName componentName = new ComponentName(packageName,activityName);
        tarIntent.setComponent(componentName);
//        tarIntent.putExtra("show_progress_" ,false);
//        tarIntent.putExtra("__intent_url__" ,"file:///android_asset/data/start_off/yishi.html");
//        tarIntent.putExtra("__intent_title__" ,"驾照遗失");
//        tarIntent.putExtra("__intent_show_title__" ,false);
//        tarIntent = getTarIntent(activityName);

        openActivity.setAction(MonitorActivityService.openByIntent);
        openActivity.putExtra(MonitorActivityService.targetPackageName,packageName);
        openActivity.putExtra(MonitorActivityService.targetActivityName,activityName);
        openActivity.putExtra("tarIntent",tarIntent);
        openActivity.putExtra("time",time);
        Log.i("LZH","time "+time);
        sendBroadcast(openActivity);

        PackageManager packageManager = getPackageManager();
        Intent intent ;
//        com.douban.movie
        intent = packageManager.getLaunchIntentForPackage(packageName);
        startActivity(intent);

    }
    private Intent getTarIntent(String ActivityName){
        SavePreference savePreference = SavePreference.getInstance(getApplicationContext());
        Intent intent = savePreference.getIntent(ActivityName);
        return  intent;
    }
}
