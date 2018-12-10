package com.example.a17916.test4_hook.monitorService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.a17916.test4_hook.receive.ActivityReceiver;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.share.SavePreference;

import java.util.ArrayList;

public class MonitorActivityService extends Service{

    public static final String openActivity = "openActivity";
    public static final String opened = "hasOpened";
    public static final String openByIntent = "openActivityByIntent";
    public static final String openByIntentInfo = "openActivityByIntentInfo";

    public static final String saveIntentInfo = "saveIntentInfo";
    public static final String saveIntent = "saveIntent";
    public static final String saveMotionEvent = "saveMotionEvent";

    private String motionEventKey = "";
    private byte[] bytes;

    private static String openWay = "";
    
    private ArrayList<Intent> historyInent;

    private Intent [] testIntent = new Intent[2];

    private SavePreference savePreference;

    private MonitorActivityReceiver openActivityReceiver;
    public MonitorActivityService(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        historyInent = new ArrayList<>();
        savePreference = SavePreference.getInstance(getApplicationContext());
        openActivityReceiver = new MonitorActivityReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MonitorActivityReceiver.openByIntent);
        intentFilter.addAction(MonitorActivityReceiver.openByIntentInfo);
        intentFilter.addAction(MonitorActivityReceiver.openActivity);
        intentFilter.addAction(MonitorActivityReceiver.opened);
        intentFilter.addAction(MonitorActivityReceiver.ON_CREATE_STATE);
        intentFilter.addAction(MonitorActivityReceiver.ON_RESUME_STATE);
        intentFilter.addAction(MonitorActivityReceiver.ON_DESTROY_STATE);
        registerReceiver(openActivityReceiver,intentFilter);

        BroadcastReceiver saveReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action){
                    case MonitorActivityService.saveIntent:
                        saveIntent(intent);
                        break;
                    case MonitorActivityService.saveIntentInfo:
                        break;
                    case MonitorActivityService.saveMotionEvent:
                        Log.i("LZH","save MotionEvent");
                        Bundle bundle = intent.getBundleExtra("MotionEvent");
                        motionEventKey = bundle.getString("eventKey");
                        bytes = bundle.getByteArray("MotionEvents");
                        saveMotionEvent(motionEventKey,bytes);

                }
            }
        };
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(MonitorActivityService.saveIntent);
        intentFilter1.addAction(MonitorActivityService.saveIntentInfo);
        intentFilter1.addAction(MonitorActivityService.saveMotionEvent);
        registerReceiver(saveReceiver,intentFilter1);
    }

    private void saveMotionEvent(String motionEventKey, byte[] bytes) {
        savePreference.writeMotionEvent(motionEventKey,bytes);
    }

    public void openActivity(String packageName,Intent intent){
        openActivityReceiver.setTargetIntent(intent);

        Intent openApp = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(openApp);

    }
    private void openActivityByIntentInfo() {
        Intent broadcast = new Intent();
        broadcast.setAction(LocalActivityReceiver.openTargetActivityByIntentInfo);
        Bundle bundle = new Bundle();
        bundle.putString("packageName","com.douban.movie");//com.douban.movie
//        com.douban.frodo.subject.activity.LegacySubjectActivity
        bundle.putString("tarActivityName","com.douban.frodo.subject.activity.LegacySubjectActivity");
        bundle.putString("dataUri","douban://douban.com/movie/3168101");
        broadcast.putExtras(bundle);
        sendBroadcast(broadcast);
    }

    private void openActivityByIntent() {
        if(historyInent.size()<=0){
            Log.i("LZH","historyInent size is 0");
            return;
        }
        Intent openActivity = new Intent();
        openActivity.putExtra("opendActivityIntent",historyInent.get(historyInent.size()-1));
        openActivity.setAction(LocalActivityReceiver.openTargetActivityByIntent);
        sendBroadcast(openActivity);
//        Log.i("LZH","open activity By Intent");

    }

    private void saveIntent(Intent intent) {
        Intent opendIntent = intent.getParcelableExtra("opendActivityIntent");
        String openActivityName = intent.getStringExtra("openActivityName");
        String key = openActivityName;
        if(opendIntent==null){
            Log.i("LZH",openActivityName+" intent is null ");
        }
        SavePreference preference = SavePreference.getInstance(this.getApplicationContext());
        preference.writeIntent(key,opendIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Log.i("LZH","start Service");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new TransportBinder();
    }
    private String getIntentInfo(Intent intent){
        String res = "";
        ComponentName componentName = intent.getComponent();

        String tarActivity = componentName.getClassName();
        String action = intent.getAction();
        String type = intent.getType();
        String data = intent.getDataString();
        String scheme = intent.getScheme();
        return res+="action: "+action+" type: "+type+" data: "+data+" scheme: "+scheme+" tarActivity: "+tarActivity;
    }

    public class TransportBinder extends Binder{

        public MonitorActivityService getService(){
            return MonitorActivityService.this;
        }
    }
}
