package com.example.a17916.test4_hook.xposed;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import com.example.a17916.test4_hook.monitorService.MonitorActivityReceiver;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.receive.CreateTempleReceiver;
import com.example.a17916.test4_hook.receive.InputTextReceiver;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by vector on 16/8/4.
 * 只用来配合工具，主要是用来查看页面结构和打印intent序列
 */
public class ActivityOnCreateHook extends XC_MethodHook {

    XC_LoadPackage.LoadPackageParam loadPackageParam;
    
    public ActivityOnCreateHook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.loadPackageParam = loadPackageParam;
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }
    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);

        final Context context = (Context) param.thisObject;

        Activity activity = (Activity) param.thisObject;
        ComponentName componentName = activity.getComponentName();
        Intent intent = new Intent();
        intent.setAction(MonitorActivityService.ON_CREATE_STATE);
        intent.putExtra(MonitorActivityService.CREATE_PACKAGE_NAME,componentName.getPackageName());
        intent.putExtra(MonitorActivityService.CREATE_ACTIVITY_NAME,componentName.getClassName());
        activity.sendBroadcast(intent);

//        KLog.v("liuyi","=======onCreate========: " + activityName);


//        Log.i("LZH",activity.getComponentName().getPackageName());
//        print(activity.getComponentName().getClassName(),intent);


//        KLog.v(BuildConfig.GETVIEW, "#*#*#*#*#*#*# enable receiver in: " + activityName);
        injectReceiver(context, activity);
        injectCreateTempleReceiver(activity);
        testinject(activity);
    }

    private void testinject(Activity activity) {
        InputTextReceiver inputTextReceiver = new InputTextReceiver(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(InputTextReceiver.INPUT_TEXT);
        activity.registerReceiver(inputTextReceiver,filter);
    }

    private void injectReceiver(Context context, Activity activity) {
        //注册一个广播接收器，可以用来接收指令，这里是用来回去指定view的xpath路径的
//        BroadcastReceiver receiver = new ActivityReceiver(activity);
//        IntentFilter filter = new IntentFilter(ActivityReceiver.myAction);
//        filter.addAction(ActivityReceiver.findView);
//        filter.addAction(ActivityReceiver.getIntent);
//        filter.addAction(ActivityReceiver.getViewTree);

//        XposedHelpers.setAdditionalInstanceField(activity, "iasReceiver", receiver);

//        context.registerReceiver(receiver, filter);


        ComponentName componentName = activity.getComponentName();
        Intent intent = activity.getIntent();
//        Log.i("LZH","packageName: "+componentName.getPackageName()+" intent "+getIntentInfo(intent));
//        Log.i("LZH","openActivity: "+componentName.getClassName());

        LocalActivityReceiver receiver = new LocalActivityReceiver(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocalActivityReceiver.findView);
        filter.addAction(LocalActivityReceiver.intent);
        filter.addAction(LocalActivityReceiver.viewTree);
        filter.addAction(LocalActivityReceiver.currentActivity);
        filter.addAction(LocalActivityReceiver.openTargetActivityByIntent);
        filter.addAction(LocalActivityReceiver.openTargetActivityByIntentInfo);
        filter.addAction(LocalActivityReceiver.INPUT_TEXT);
        filter.addAction(LocalActivityReceiver.INPUT_EVENT);
        filter.addAction(LocalActivityReceiver.GenerateIntentData);
        XposedHelpers.setAdditionalInstanceField(activity, "iasReceiver", receiver);
        activity.registerReceiver(receiver,filter);
//        Log.i("LZH","register activity: "+componentName.getClassName());
    }
    private void injectCreateTempleReceiver(Activity activity){

        CreateTempleReceiver receiver = new CreateTempleReceiver(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction(CreateTempleReceiver.CREATE_TEMPLE);
        filter.addAction(MonitorActivityService.ON_RESUME_STATE);
        XposedHelpers.setAdditionalInstanceField(activity, "iasCreateTempleReceiver", receiver);
        activity.registerReceiver(receiver,filter);
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
    private void print(String activityName,Intent intent){
        Log.i("LZH","com.handsgo.jiakao.android.MyWebView");
        if(activityName.compareTo("com.handsgo.jiakao.android.MyWebView")!=0){
            return ;
        }
        String video_path = intent.getStringExtra("video_path");
        boolean show_progress_ =intent.getBooleanExtra("show_progress_",true);
        String __intent_url__ = intent.getStringExtra("__intent_url__" );
        String __intent_title__ = intent.getStringExtra("__intent_title__" );
        String page_name = intent.getStringExtra("page_name" );
        boolean __intent_show_title__ = intent.getBooleanExtra("__intent_show_title__" ,true);
        Log.i("LZH","VP: "+video_path+" SP: "+show_progress_+" iU: "+__intent_url__+" iT: "+__intent_title__+" PN: "+
                page_name+" iST: "+__intent_show_title__);
    }
}
