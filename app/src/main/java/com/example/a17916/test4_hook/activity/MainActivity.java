package com.example.a17916.test4_hook.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.a17916.test4_hook.R;
import com.example.a17916.test4_hook.TempGenerateDataBase.GenerateDataService;
import com.example.a17916.test4_hook.ViewManager.FloatViewManager;
import com.example.a17916.test4_hook.application.MyApplication;
import com.example.a17916.test4_hook.database.AppData;
import com.example.a17916.test4_hook.database.QueryManager;
import com.example.a17916.test4_hook.manageActivity.ActivityController;
import com.example.a17916.test4_hook.manageActivity.ControllerService;
import com.example.a17916.test4_hook.matchModule.taskModule.TaoPiaoPiao.TestTPP;
import com.example.a17916.test4_hook.matchModule.taskModule.TaskFactory;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.monitorService.MyActivityHandler;
import com.example.a17916.test4_hook.monitorService.OpenActivityTask;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.share.SavePreference;
import com.greendao.gen.AppDataDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String packageName = "";
    private String activityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartSendDataService();
        startSaveData();

    }

    public void startFloatBt(View view){
        FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
        floatViewManager.showFloatButton();
    }
    public void createTempleBt(View view){
        FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
        floatViewManager.showCreateTempleBt();
    }

    private void StartSendDataService(){
        Intent intent = new Intent(this,ControllerService.class);
        startService(intent);
    }

    private void startSaveData(){
        Intent intent = new Intent(this,GenerateDataService.class);
        startService(intent);
    }
    public void openDBMovie(View view){
        PackageManager packageManager = getPackageManager();

        Intent intent = new Intent();
//        com.douban.movie
        intent = packageManager.getLaunchIntentForPackage("com.douban.movie");
        startActivity(intent);
    }
    public void openByIntent(View view){
//        ActivityController controller = ActivityController.getInstance(getApplicationContext());
//        OpenActivityTask task = TaskFactory.getTaskByActivityName("com.douban.frodo.subject.activity.LegacySubjectActivity",getApplicationContext());
//        task.setSearchText("海王");
//        task.setMyHandler(MyActivityHandler.getInstance());
//        controller.addTask("com.douban.movie","com.douban.frodo.subject.activity.LegacySubjectActivity",task);

        QueryManager queryManager = QueryManager.getInstance();
        List<Intent> intents = queryManager.queryIntents("云南虫谷");

        if(intents.size()<1){
            Log.i("LZH","查询不到数据");
        }else{
            Log.i("LZH","查询到的Intent的数量: "+intents.size());
        }

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
