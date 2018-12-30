package com.example.a17916.test4_hook.TempGenerateDataBase;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.a17916.test4_hook.monitorService.MonitorActivityService;

public class GenerateDataService extends Service {
    public static final String GENERATE_DATA = "GENERATE_DATA";
    public static final String PAGE_INFO = "PAGE_INFO";
    public static final String ACTIVITY_NAME = "ACTIVITY_NAME";
    public static final String VERSION = "VERSION";
    public static final String APP_NAME = "APP_NAME";

    public static final String SAVE_INTENT = "SAVE_INTENT";
    public static final String NEED_SAVE_INTENT = "NEED_SAVE_INTENT";
    private GenerateDataReceiver receiver;
    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new GenerateDataReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(GenerateDataService.GENERATE_DATA);
        intentFilter.addAction(GenerateDataService.SAVE_INTENT);
        registerReceiver(receiver,intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
