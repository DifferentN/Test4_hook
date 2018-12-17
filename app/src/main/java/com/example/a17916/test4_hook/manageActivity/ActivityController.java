package com.example.a17916.test4_hook.manageActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ActivityController {
    private Context context;
    private static ActivityController activityController;
    public static final String SEND_ACTIVITY_INTENT = "sendActivityIntent";
    public static final String SEND_INTENT_MOTION = "sendIntentMotion";
    public static final String TARGET_INTENT = "targetIntent";
    public static final String PK_NAME = "packageName";
    public static final String TEXT = "TEXT";

    private String currentActivityName;
    public ActivityController(Context context){
        this.context = context;
    }
    public static ActivityController getInstance(Context context){
        if(activityController == null){
            activityController = new ActivityController(context);
        }
        return activityController;
    }
    public void openActivity(String packageName,Intent intent){
        Intent broadIntent = new Intent();
        broadIntent.setAction(ActivityController.SEND_ACTIVITY_INTENT);
        broadIntent.putExtra(ActivityController.TARGET_INTENT,intent);
        broadIntent.putExtra(ActivityController.PK_NAME,packageName);
        context.sendBroadcast(broadIntent);
    }

    /**
     *
     * @param packageName
     * @param intent
     * @param key  为搜索词，用来填入搜索框，并作为key值的一部分获得motionEvent
     */
    public void openActivityWithMotionEvent(String packageName,Intent intent,String key){
        Intent broadIntent = new Intent();
        broadIntent.setAction(ActivityController.SEND_INTENT_MOTION);
        broadIntent.putExtra(ActivityController.TARGET_INTENT,intent);
        broadIntent.putExtra(ActivityController.PK_NAME,packageName);
        broadIntent.putExtra(ActivityController.TEXT,key);
        Log.i("LZH","打开的Key: "+key);
        context.sendBroadcast(broadIntent);
    }


}
