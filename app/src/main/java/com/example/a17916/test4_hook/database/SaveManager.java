package com.example.a17916.test4_hook.database;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.example.a17916.test4_hook.application.MyApplication;
import com.example.a17916.test4_hook.util.normal.ParcelableUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class SaveManager {
    private static SaveManager saveManager;
    private SQLiteDatabase database;
    public static SaveManager getInstance(){
        if(saveManager==null){
            saveManager = new SaveManager();
        }
        return saveManager;
    }
    public SaveManager(){
        MyApplication myApplication = MyApplication.getInstance();
        database = myApplication.getSqLiteDatabase();
    }

    public void saveMotionEvent(String activityName,String resType,int seq,byte[] bytes){
        MotionData motionData = null;
        Cursor cursor = database.query(MotionData.MotionTable,null,
                MotionData.ActivityName+"=? and "+MotionData.ResType+" =? and "+MotionData.MotionSeq+" =?",
                new String[]{activityName,resType,seq+""},
                null,null,null);
        ContentValues values = new ContentValues();
        String motionStr = Base64.encodeToString(bytes,0);
        int id = -1;
        if(cursor.moveToNext()){
            values.put(MotionData.MotionByte,motionStr);
            id = cursor.getInt(cursor.getColumnIndex(MotionData.MotionId));
            database.update(MotionData.MotionTable,values,MotionData.MotionId+"=?",new String[]{id+""});
            Log.i("LZH","更新点击事件： "+id);
        }else{

            values.put(MotionData.MotionByte,motionStr);
            values.put(MotionData.ActivityName,activityName);
            values.put(MotionData.ResType,resType);
            values.put(MotionData.MotionSeq,seq);
            long result = database.insert(MotionData.MotionTable,null,values);

            if(result ==-1){
                Log.i("LZH","插入点击事件失败： "+result);
            }
            Cursor cursor1 = database.query(MotionData.MotionTable,null,null,null,null,null,null);
            id = cursor1.getCount();
            Log.i("LZH","插入点击事件： "+id);
            cursor1.close();
        }
        cursor.close();
    }
    /**
     * 添加一个新的APP记录
     * @param appName
     * @param version
     * @return
     */
    public AppData addAppData(String appName, String version,String pkName){
        AppData appData = null;
        Cursor cursor = database.query(AppData.tableName,null,
                AppData.AppName+"=? and "+AppData.Version+" =? and "+AppData.PackageName+" =?",
                new String[]{appName,version,pkName},
                null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToNext()){
            appData = new AppData(cursor.getInt(cursor.getColumnIndex(AppData.AppId)),
                    cursor.getString(cursor.getColumnIndex(AppData.AppName)),
                    cursor.getString(cursor.getColumnIndex(AppData.Version)),
                    cursor.getString(cursor.getColumnIndex(AppData.PackageName)));
//            Log.i("LZH","已经存在App，你不许插入");
        }else{
            values.put(AppData.AppName,appName);
            values.put(AppData.Version,version);
            values.put(AppData.PackageName,pkName);
            database.insert(AppData.tableName,null,values);

            Cursor cursor1 = database.query(AppData.tableName,null,null,null,null,null,null);
            int id = cursor1.getCount();
            appData = new AppData(id,appName,version,pkName);
            cursor1.close();
        }
        cursor.close();

        return appData;
    }
    /**
     * 对一个App添加一个新的资源
     * @param appData 对应的APP
     * @param resEntityName 资源实体的名称
     * @param resType 资源的类型
     * @return 添加的资源条目
     */
    public ResourceData addResData(AppData appData,String resEntityName,String resType){
        ResourceData resourceData = null;
        int appId = appData.getAppId();

        Cursor cursor = database.query(ResourceData.ResourceTable,null,
                ResourceData.AppId+"=? and "+ResourceData.ResCategory+" =? and "+ResourceData.ResEntityName+" =? ",
                new String[]{appId+"",resType, resEntityName},
                null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToNext()){
            resourceData = new ResourceData(cursor.getInt(cursor.getColumnIndex(ResourceData.ResId)),
                    cursor.getInt(cursor.getColumnIndex(ResourceData.AppId)),
                    cursor.getString(cursor.getColumnIndex(ResourceData.ResCategory)),
                    cursor.getString(cursor.getColumnIndex(ResourceData.ResEntityName)));
        }else {
            values.put(ResourceData.AppId,appId);
            values.put(ResourceData.ResCategory,resType);
            values.put(ResourceData.ResEntityName,resEntityName);
            database.insert(ResourceData.ResourceTable,null,values);

            Cursor cursor1 = database.query(ResourceData.ResourceTable,null,null,null,null,null,null);
            int id = cursor1.getCount();
            resourceData = new ResourceData(id,appId,resType,resEntityName);
            cursor1.close();
        }
        cursor.close();

        return resourceData;
    }

    /**
     * 给页面添加一个新的资源
     * @param appData
     * @param resourceData
     * @param activityName
     * @return
     */
    public ActivityData addJoinResActivity(AppData appData, ResourceData resourceData, String activityName){
        ActivityData activityData = null;
        int appId = appData.getAppId();
        int resId = resourceData.getResId();
        Cursor cursor = database.query(ActivityData.tableName,null,
                ActivityData.AppId+"=? and "+ActivityData.ResId+" =? and "+ActivityData.ActivityName+" =?",
                new String[]{appId+"",resId+"",activityName},
                null,null,null);
        ContentValues values = new ContentValues();
        if(cursor.moveToNext()){
            Log.i("LZH","对该页面添加过该资源");
            activityData = new ActivityData(cursor.getInt(cursor.getColumnIndex(ActivityData.ActivityId)),
                    cursor.getString(cursor.getColumnIndex(ActivityData.ActivityName)),
                    cursor.getInt(cursor.getColumnIndex(ActivityData.AppId)),
                    appData,resId);
        }else{

//            values.put(ActivityData.ActivityId,id);
            values.put(ActivityData.AppId,appId);
            values.put(ActivityData.ResId,resId);
            values.put(ActivityData.ActivityName,activityName);
//            Log.i("LZH","添加的页面资源： appID "+appId+" resId: "+resId+" activityName: "+activityName);
            long result = database.insert(ActivityData.tableName,null,values);

            Log.i("LZH","插入结果： "+result);
            Cursor cursor1 = database.query(ActivityData.tableName,null,null,null,null,null,null);
            int id = cursor1.getCount();
            activityData = new ActivityData(id, activityName, appId, appData,resId);
            cursor1.close();

            Log.i("LZH","对该页面添加一个新的资源");

        }
        cursor.close();
        return activityData;
    }

    /**
     * 插入一个Intent资源
     * @param resourceData
     * @param activityData
     * @param intent
     */
    public void addInentData(ResourceData resourceData,ActivityData activityData,Intent intent){
        List<IntentData> intentDatas;
        IntentData intentData;
        int activityId = activityData.getActivityId();
        int resId = resourceData.getResId();

        Cursor cursor = database.query(IntentData.IntentTable,null,
                IntentData.ActivityId+"=? and "+IntentData.ResId+"=?",
                new String[]{activityId+"",resId+""},
                null,null,null);
        ContentValues values = new ContentValues();
        String intentStr = null;
        byte[] intentByte = null;
        if(cursor.moveToNext()){

        }else{
            intentByte = ParcelableUtil.intent2Bytes(intent);
            intentStr = Base64.encodeToString(intentByte,0);

            values.put(IntentData.ActivityId,activityId);
            values.put(IntentData.ResId,resId);
            values.put(IntentData.IntentByte,intentStr);
            database.insert(IntentData.IntentTable,null,values);
        }
        cursor.close();
    }
}
