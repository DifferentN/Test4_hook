package com.example.a17916.test4_hook.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.a17916.test4_hook.R;
import com.example.a17916.test4_hook.TempGenerateDataBase.GenerateDataService;
import com.example.a17916.test4_hook.TempGenerateDataBase.TestShowDataBase;
import com.example.a17916.test4_hook.ViewManager.FloatViewManager;
import com.example.a17916.test4_hook.manageActivity.ActivityController;
import com.example.a17916.test4_hook.manageActivity.ControllerService;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.openTaskModule.StepContent;
import com.example.a17916.test4_hook.openTaskModule.TaskGenerator;
import com.example.a17916.test4_hook.openTaskModule.UnionOpenActivityTask;
import com.example.a17916.test4_hook.openTaskModule.UnionTaskBuilder;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.share.SavePreference;

public class MainActivity extends AppCompatActivity {
    private EditText editText;

    private String packageName = "";
    private String activityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartSendDataService();
        startSaveData();
        editText = findViewById(R.id.editText);

        TestShowDataBase testShowDataBase = TestShowDataBase.getInstance();
        testShowDataBase.showData();
    }

    public void startFloatBt(View view){
        FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
        floatViewManager.showFloatButton();
    }
    public void createTempleBt(View view){
        FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
        floatViewManager.showCreateTempleBt();
    }

    public void saveMotionEvent(View view){
        FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
        floatViewManager.showSaveMotionViewBt();
    }
    public void saveIntent(View view){
        FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
        floatViewManager.showSaveIntentViewBt();
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

//        TaskGenerator taskGenerator = new TaskGenerator(this.getApplicationContext());
//
//        UnionOpenActivityTask task = taskGenerator.generatorTask("com.yongche.android.YDBiz.Order.HomePage.MainActivity","UseCar","房山",0);
//        ActivityController controller = ActivityController.getInstance(getApplicationContext());
//        controller.addTask("com.yongche.android","",task);

        SavePreference savePreference = SavePreference.getInstance(this.getApplicationContext());
        Intent intent = savePreference.getIntent("com.douban.frodo.baseproject.image.SociableImageActivity");
        String appName = "豆瓣电影";

//        intent.putExtra("",new Bundle());

        UnionTaskBuilder builder = new UnionTaskBuilder(this);
        builder.addIntentStep(intent,"com.douban.movie.activity.MainActivity",appName);
        UnionOpenActivityTask task = builder.generateTask();
        ActivityController controller = ActivityController.getInstance(getApplicationContext());
        controller.addTask("com.douban.movie",null,task);



//        Intent intent = getPackageManager().getLaunchIntentForPackage("com.douban.movie");
//        startActivity(intent);
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
    public void clickOpen(View view){
        String activityName = "com.douban.frodo.subject.activity.LegacySubjectActivity";
        String specialKey = "白蛇：缘起";//白蛇：缘起   大黄蜂

        String key = "<"+activityName+">1"+"/"+specialKey+"/"+"Intent";
        key = editText.getText().toString();
        Log.i("LZH","使用的："+key);
        SavePreference savePreference = SavePreference.getInstance(this.getApplicationContext());

        Intent intent = savePreference.getIntent(key);
        if(intent==null){
            Log.i("LZH","得不到JSON创建的Intent");
        }
        //找出appName
        String keys[] = key.split("/");
        String newKey = keys[0]+"/"+keys[1]+"/"+"JSON";
        String jsonStr = savePreference.readJSONStr(newKey);
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        JSONObject jsonObject = (JSONObject) jsonArray.get(0);

        String appName = jsonObject.getString("appName");
        Log.i("LZH","appName: "+appName);

        UnionTaskBuilder builder = new UnionTaskBuilder(this);
        builder.addIntentStep(intent,"com.douban.movie.activity.MainActivity",appName);
        UnionOpenActivityTask task = builder.generateTask();
        ActivityController controller = ActivityController.getInstance(getApplicationContext());

        String pkName = intent.getComponent().getPackageName();

        controller.addTask(pkName,null,task);

    }
}
