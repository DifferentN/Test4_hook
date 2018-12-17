package com.example.a17916.test4_hook.monitorService;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;

import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.share.SavePreference;

import java.util.ArrayList;
import java.util.List;

public class MonitorActivityService extends Service{

    public static final String openActivity = "openActivity";
    public static final String opened = "hasOpened";
    public static final String openByIntent = "openActivityByIntent";
    public static final String openByIntentInfo = "openActivityByIntentInfo";

    public static final String saveIntentInfo = "saveIntentInfo";
    public static final String SAVE_INTENT = "saveIntent";
    public static final String SAVE_MOTION_EVENT = "saveMotionEvent";
    public static final String MOTION_EVENT = "MOTION_EVENT";
    public static final String EVENT_ACTIVITY = "EVENT_ACTIVITY";
    public static final String SAVE_EDIT_TEXT = "SAVE_EDITTEXT";
    public static final String EDIT_TEXT = "EDIT_TEXT";

    public static final String ON_CREATE_STATE = "onCreateActivity";
    public static final String ON_RESUME_STATE = "onResumeActivity";
    public static final String ON_DESTROY_STATE = "onDestroyActivity";

    public static final String OPENED_PACKAGE_NAME = "openedPackageName";
    public static final String OPENED_ACTIVITY_NAME = "openedActivityName";
    public static final String DESTROY_PACKAGE_NAME = "destroyPackageName";
    public static final String DESTROY_ACTIVITY_NAME = "destroyActivityName";

    private String motionEventKey = "";
    private byte[] bytes;

    private static String openWay = "";
    
    private ArrayList<Intent> historyInent;

    private Intent [] testIntent = new Intent[2];

    private SavePreference savePreference;

    private MonitorActivityReceiver openActivityReceiver;
    private SaveMotionReceiver saveMotionRecevier;
    public MonitorActivityService(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        historyInent = new ArrayList<>();
        savePreference = SavePreference.getInstance(getApplicationContext());
        init();


    }

    private void init(){
        openActivityReceiver = new MonitorActivityReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MonitorActivityService.openByIntent);
        intentFilter.addAction(MonitorActivityService.openByIntentInfo);
        intentFilter.addAction(MonitorActivityService.openActivity);
        intentFilter.addAction(MonitorActivityService.opened);
        intentFilter.addAction(MonitorActivityService.ON_CREATE_STATE);
        intentFilter.addAction(MonitorActivityService.ON_RESUME_STATE);
        intentFilter.addAction(MonitorActivityService.ON_DESTROY_STATE);
        registerReceiver(openActivityReceiver,intentFilter);

        saveMotionRecevier = new SaveMotionReceiver(this);
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(MonitorActivityService.SAVE_INTENT);
        intentFilter1.addAction(MonitorActivityService.saveIntentInfo);
        intentFilter1.addAction(MonitorActivityService.SAVE_MOTION_EVENT);
        intentFilter1.addAction(MonitorActivityService.SAVE_EDIT_TEXT);
        intentFilter1.addAction(MonitorActivityService.EDIT_TEXT);
        intentFilter1.addAction(MonitorActivityService.ON_CREATE_STATE);
        intentFilter1.addAction(MonitorActivityService.ON_RESUME_STATE);
        registerReceiver(saveMotionRecevier,intentFilter1);
    }


    public void openActivity(String packageName,Intent intent){
        openActivityReceiver.setTargetIntent(intent);
        Intent openApp = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(openApp);
    }

    public void openActivityWithMotionEvent(String packageName,Intent intent,String text){
        ComponentName componentName = intent.getComponent();
        openActivityReceiver.setMotionEvent(componentName.getClassName(),text);
        openActivity(packageName,intent);

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
