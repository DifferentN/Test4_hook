package com.example.a17916.test4_hook.util.normal;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


import java.util.List;


/**
 * Created by vector on 16/12/28.
 */

public class ActivityUtil {
    /**
     *通过RunningAppProcessInfo类判断（不需要额外权限）：
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("liuyi", "后台：" + appProcess.processName);
                    return true;
                } else {
                    Log.i("liuyi", "前台：" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className
     *            某个界面名称
     */
    public static boolean isForeground(Context context, String className) {

        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }



    //这个只能判断当前应用是否在前台,没法精确到当个Activity
    public static  boolean isAppIsInBackground(Context context, String className, String packageName) {
        boolean isInBackground = true;
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
//            return !DetectionService.isForegroundPkgViaDetectionService(packageName);
//            //return !DetectionService.isForegroundActivityDetection(className);
//        } else {
//            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//            ComponentName componentInfo = taskInfo.get(0).topActivity;
//            if (componentInfo.getClassName().equals(className)) {
//                isInBackground = false;
//            }
//        }

        return isInBackground;
    }
}
