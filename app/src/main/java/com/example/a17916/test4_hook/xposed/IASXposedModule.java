package com.example.a17916.test4_hook.xposed;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.a17916.test4_hook.diskSave.DiskSave;
import com.example.a17916.test4_hook.monitorService.MonitorActivityReceiver;
import com.example.a17916.test4_hook.receive.LocalActivityReceiver;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.util.activityView.ActivityCache;

import java.lang.reflect.Field;
import java.util.ArrayList;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class IASXposedModule implements IXposedHookLoadPackage {

    public static String className="";
    public DiskSave diskSave = new DiskSave(DiskSave.getShareFile());
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Log.i("LZH","Loaded app: "+loadPackageParam.packageName);
        XposedBridge.log("=========Loaded app: " + loadPackageParam.packageName);

        if (loadPackageParam.packageName.equals("com.example.a17916.test3_1")) {

            Class clazz = loadPackageParam.classLoader.loadClass("com.example.a17916.test3_1.MainActivity");
        }

        XposedHelpers.findAndHookMethod("android.app.Activity", loadPackageParam.classLoader, "onCreate", Bundle.class, new ActivityOnCreateHook(loadPackageParam));
        XposedHelpers.findAndHookMethod("android.app.Activity", loadPackageParam.classLoader, "onResume", new ActivityOnResumeHook());
        XposedHelpers.findAndHookMethod("android.app.Activity",loadPackageParam.classLoader,"dispatchTouchEvent",MotionEvent.class,new ActivityOnTouchEventHook());
//        XposedHelpers.findAndHookMethod("android.support.v7.widget.RecyclerView",loadPackageParam.classLoader,"onTouchEvent",MotionEvent.class,new RecyclerViewOnTouchEventHook());
//        XposedHelpers.findAndHookMethod("android.view.ViewGroup",loadPackageParam.classLoader,"dispatchTouchEvent",MotionEvent.class,new ViewGroupDispatchTouchEventHook());
//        XposedHelpers.findAndHookMethod("android.view.ViewGroup",loadPackageParam.classLoader,"dispatchTransformedTouchEvent",MotionEvent.class,boolean.class,View.class,int.class,new VGdispatchTransformedTouchEventHook());
//        XposedHelpers.findAndHookMethod("android.view.ViewGroup",loadPackageParam.classLoader,"getTouchTarget",View.class,new VGgetTouchTargetHook());

//        hook_activityThread_OnCreate(loadPackageParam.classLoader.loadClass("android.app.Instrumentation"));
//        hook_activityThread_OnResume(loadPackageParam.classLoader.loadClass("android.app.Instrumentation"));
        hook_activityThread_OnDestroy(loadPackageParam.classLoader.loadClass("android.app.Instrumentation"));
//        hook_mainHandler(loadPackageParam.classLoader.loadClass("android.os.Looper"));
        XposedHelpers.findAndHookMethod("android.app.Activity", loadPackageParam.classLoader, "onDestroy", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
                Activity activity = (Activity) param.thisObject;
                ComponentName componentName = activity.getComponentName();
//                Log.i("LZH",activity.getComponentName().getClassName()+" unregisterReceiver ");
                LocalActivityReceiver receiver = (LocalActivityReceiver) XposedHelpers.getAdditionalInstanceField(param.thisObject,"iasReceiver");
                if(receiver != null) {
                    activity.unregisterReceiver(receiver);
                    XposedHelpers.setAdditionalInstanceField(param.thisObject, "iasReceiver", null);
                }
                Intent broad = new Intent();
                broad.setAction(MonitorActivityReceiver.ON_DESTROY_STATE);
                broad.putExtra(MonitorActivityReceiver.DESTROY_PACKAGE_NAME,componentName.getPackageName());
                broad.putExtra(MonitorActivityReceiver.DESTROY_ACTIVITY_NAME,componentName.getClassName());
                activity.sendBroadcast(broad);
            }
        });
    }


    private void hook_mainHandler(Class clazz){
        XposedHelpers.findAndHookMethod(clazz, "prepareMainLooper",  new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                Log.i("LZH","mainHandler");
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Activity activity = (Activity) param.getResult();
//                Log.i("LZH","mainHandler_PKName: "+activity.getPackageName());
//                ActivityThread a;
                Class looperClass = param.thisObject.getClass();
                Field mainLoop = looperClass.getDeclaredField("Looper");
                mainLoop.setAccessible(true);
                Looper tempLooper = (Looper) mainLoop.get(param.thisObject);
                Log.i("LZH","get Looper");
            }
        });
    }



    private void hook_DecorView(Class clazz){
        Log.i("LZH","hook decorView");
        XposedHelpers.findAndHookMethod(clazz, "addView", View.class, ViewGroup.LayoutParams.class,
                Display.class, Window.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        super.afterHookedMethod(param);
                        Class clazz = param.thisObject.getClass();
                        Field views = clazz.getDeclaredField("mViews");
                        views.setAccessible(true);
                        ArrayList<View> mViews = (ArrayList<View>) views.get(param.thisObject);

//                        Log.i("LZH","after add view "+mViews.size());
                    }
                });
    }
    private void hook_activityThread_OnCreate(final Class<?> aClass) {
        XposedHelpers.findAndHookMethod(aClass, "callActivityOnCreate", Activity.class,Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity)param.args[0];

                ComponentName componentName = activity.getComponentName();
                Intent intent = activity.getIntent();
//                Log.i("LZH","packageName: "+componentName.getPackageName()+" intent "+getIntentInfo(intent));
                Log.i("LZH","openActivity: "+componentName.getClassName());

                LocalActivityReceiver receiver = new LocalActivityReceiver(activity);
                IntentFilter filter = new IntentFilter();
                filter.addAction(LocalActivityReceiver.findView);
                filter.addAction(LocalActivityReceiver.intent);
                filter.addAction(LocalActivityReceiver.viewTree);
                filter.addAction(LocalActivityReceiver.currentActivity);
                filter.addAction(LocalActivityReceiver.openTargetActivityByIntent);
                filter.addAction(LocalActivityReceiver.openTargetActivityByIntentInfo);
                XposedHelpers.setAdditionalInstanceField(activity, "iasReceiver", receiver);
                activity.registerReceiver(receiver,filter);
            }
        });
    }
    private void hook_activityThread_OnResume(Class clazz){
        XposedHelpers.findAndHookMethod(clazz, "callActivityOnResume", Activity.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Activity activity = (Activity)param.args[0];
                ComponentName componentName = activity.getComponentName();
//                View mDecorView = activity.getWindow().getDecorView();
//                MyViewTree viewTree = new MyViewTree(mDecorView,componentName.getPackageName());
//                MyViewNode viewNode = viewTree.getViewNode();
                Intent intent = new Intent();
                intent.setAction(LocalActivityReceiver.currentActivity);
                Bundle bundle = new Bundle();
                bundle.putString("showActivity",componentName.getClassName());
                bundle.putString("curPackage",componentName.getPackageName());
                intent.putExtra("currentActivity",bundle);
                activity.sendBroadcast(intent);

                //给服务发送打开命令，打开指定activity
                Intent broad = new Intent();
                broad.setAction(MonitorActivityService.openActivity);
//                broad.setAction(OpenActivityService.testOpen);
                activity.sendBroadcast(broad);

                //测试中，保存打开应用的Intent
                Intent saveIntent = new Intent();
                saveIntent.setAction(MonitorActivityService.saveIntent);
                saveIntent.putExtra("opendActivityIntent",activity.getIntent());
                saveIntent.putExtra("openActivityName",activity.getComponentName().getClassName());
                activity.sendBroadcast(saveIntent);
//                saveIntent(componentName.getClassName(),activity.getIntent());



            }
        });
    }
    private void hook_activityThread_OnDestroy(Class clazz){
        XposedHelpers.findAndHookMethod(clazz, "callActivityOnDestroy", Activity.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ActivityCache activityCache = ActivityCache.getInstance();
                Activity activity = (Activity)param.args[0];
                activityCache.remove(activity);
                ComponentName componentName = activity.getComponentName();
//                View mDecorView = activity.getWindow().getDecorView();
//                MyViewTree viewTree = new MyViewTree(mDecorView,componentName.getPackageName());
//                MyViewNode viewNode = viewTree.getViewNode();

            }
        });
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

    private void saveIntent(String key,Intent intent){
        diskSave.writeIntent(DiskSave.trimKey(key).toLowerCase(),intent);
        Log.i("LZH","file path "+DiskSave.getShareFile().getAbsolutePath());
        Log.i("LZH","save Intent "+key);
    }


}
