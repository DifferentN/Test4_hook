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
import com.example.a17916.test4_hook.manageActivity.ActivityController;
import com.example.a17916.test4_hook.manageActivity.ControllerService;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
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
        Intent intent = new Intent(this,ControllerService.class);
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
        ActivityController controller = ActivityController.getInstance(getApplicationContext());
        controller.openActivity("com.douban.movie",getTarIntent("com.douban.frodo.subject.activity.LegacySubjectActivity"));
//        openCreateIntent();
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

        openActivity.setAction(MonitorActivityService.openByIntent);
        openActivity.putExtra(LocalActivityReceiver.targetPackageName,packageName);
        openActivity.putExtra(LocalActivityReceiver.targetActivityName,activityName);
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
