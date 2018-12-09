package com.example.a17916.test4_hook.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.a17916.test4_hook.R;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.share.SavePreference;

public class ShowActivity extends AppCompatActivity {
    private Intent tarIntent;
    private TextView textView;
    private String tarActivityName,packageName,activityName;
    private int time = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_info);
        Intent intent = getIntent();
        String text = intent.getStringExtra("info");
        textView = findViewById(R.id.TextView_info);
        textView.setText(text);

    }
    public void openTarActivity(View view){
        if(time == 0){
            tarActivityName = "com.douban.frodo.subject.activity.LegacySubjectActivity";
            packageName = "com.douban.movie";
            activityName = "com.douban.frodo.subject.activity.LegacySubjectActivity";

        }else if(time == 1){
            tarActivityName = "com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity";
            packageName = "com.yongche.android";
            activityName = "com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity";
        }else{
            tarActivityName = "com.handsgo.jiakao.android.CommonList";
            packageName = "com.handsgo.jiakao.android";
            activityName = "com.handsgo.jiakao.android.CommonList";
            return;
        }

        tarIntent = getTarIntent(tarActivityName);

        if(tarIntent == null){
            Log.i("LZH","tarIntent is null");
        }
        Intent openActivity = new Intent();

        openActivity.setAction(MonitorActivityService.openByIntent);
        openActivity.putExtra(MonitorActivityService.targetPackageName,packageName);
        openActivity.putExtra(MonitorActivityService.targetActivityName,activityName);
        openActivity.putExtra("tarIntent",tarIntent);
        openActivity.putExtra("time",time);
        Log.i("LZH","time "+time);
        sendBroadcast(openActivity);
        time++;
        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent();
//        com.douban.movie
        intent = packageManager.getLaunchIntentForPackage(packageName);
        startActivity(intent);

    }
    private Intent getTarIntent(String ActivityName){
//        KeyPreference keyPreference = KeyPreference.getInstance(getApplicationContext());
//        String key  = keyPreference.read(ActivityName);
        SavePreference savePreference = SavePreference.getInstance(getApplicationContext());
        Intent intent = savePreference.getIntent(ActivityName);
        return  intent;
//        DiskSave diskSave = new DiskSave(DiskSave.getShareFile());
//        Intent intent = diskSave.readIntent(DiskSave.trimKey(ActivityName).toLowerCase());
//        Log.i("LZH","file path "+ACache.getShareFile().getAbsolutePath());
//        return intent;
    }

}
