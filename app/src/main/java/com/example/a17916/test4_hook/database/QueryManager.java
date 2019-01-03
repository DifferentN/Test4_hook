package com.example.a17916.test4_hook.database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;

import com.example.a17916.test4_hook.application.MyApplication;
import com.example.a17916.test4_hook.util.normal.ParcelableUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

public class QueryManager {
    private static QueryManager queryData;
    private SQLiteDatabase database;
    public static QueryManager getInstance(){
        if(queryData == null){
            queryData = new QueryManager();
        }
        return queryData;
    }
    public QueryManager(){
        MyApplication myApplication = MyApplication.getInstance();
        database = myApplication.getSqLiteDatabase();

    }

    public List<Intent> queryIntents(String resEntityName){
        List<Integer> resIds = queryResByName(resEntityName);
        List<Integer[]> resActIds = new ArrayList<>();
        List<Integer> activityIds = null;
        //搜索含有该资源的页面
        for(Integer resId:resIds){
            activityIds = queryActivityIdByResId(resId);
            for(Integer activityId:activityIds){
                resActIds.add(new Integer[]{activityId,resId});
            }
        }


        List<Intent> queryResult = new ArrayList<>();
        //根据以上信息得到可以显示该资源的Intent
        for(Integer [] result:resActIds){
            queryResult.add(queryIntent(result[0],result[1]));
        }
        return queryResult;
    }

    /**
     * 通过资源实体名称来查询其资源ID
     * @param resEntityName 资源实体名称
     * @return 资源的Id
     */
    private List<Integer> queryResByName(String resEntityName){
        Cursor cursor = database.query(ResourceData.ResourceTable,null,
                ResourceData.ResEntityName+"=?",new String[]{resEntityName},
                null,null,null);

        ArrayList<Integer> queryResult = new ArrayList<>();
        while(cursor.moveToNext()){
            queryResult.add(cursor.getInt(cursor.getColumnIndex(ResourceData.ResId)));
        }
        cursor.close();
        return queryResult;
    }

    /**
     * 通过资源实体名称来查询
     * @param resEntityName
     * @return 资源实体链
     */
    public List<ResourceData> queryResDataByName(String resEntityName){
//        Log.i("LZH","资源名称: "+resEntityName);
        Cursor cursor = database.query(ResourceData.ResourceTable,null,
                ResourceData.ResEntityName+"=?",new String[]{resEntityName},
                null,null,null);

        List<ResourceData> resourceDatas = new ArrayList<>();
        ResourceData resourceData;
        while (cursor.moveToNext()){
            resourceData = new ResourceData(cursor.getInt(cursor.getColumnIndex(ResourceData.ResId)),
                    cursor.getInt(cursor.getColumnIndex(ResourceData.AppId)),
                    cursor.getString(cursor.getColumnIndex(ResourceData.ResCategory)),
                    cursor.getString(cursor.getColumnIndex(ResourceData.ResEntityName)));
            resourceDatas.add(resourceData);
        }
        cursor.close();
        return  resourceDatas;
    }

    /**
     * 通过资源Id 来查询可以显示它的activity/页面
     * @param resId
     * @return 查找到的可以显示该资源的一系列页面的Id
     */
    private List<Integer> queryActivityIdByResId(Integer resId){
        ArrayList<Integer> queryResult = new ArrayList<>();
        Cursor cursor = database.query(ActivityData.tableName,null,
                ActivityData.ResId+"=?",new String[]{resId+""},
                null,null,null);
        while (cursor.moveToNext()){
            queryResult.add(cursor.getInt(cursor.getColumnIndex(ActivityData.ActivityId)));
        }
        cursor.close();
        return queryResult;
    }

    /**
     * 通过页面Id 查询它显示资源的Intent
     * @param activityId
     * @param resId
     * @return
     */
    public Intent queryIntent(int activityId,int resId){
        IntentData intentData;
        Intent intent = null;
        Cursor cursor = database.query(IntentData.IntentTable,null,
                IntentData.ActivityId+"=? and "+IntentData.ResId+"=?",
                new String[]{activityId+"",resId+""},
                null,null,null);
        while (cursor.moveToNext()){
            String intentStr = cursor.getString(cursor.getColumnIndex(IntentData.IntentByte));
            if(intentStr == null){
                Log.i("LZH","获得intent字段为null");
                return null;
            }
            byte[] intentBytes =  Base64.decode(intentStr,0);
            intent = ParcelableUtil.byte2Intent(intentBytes);
            break;
        }
        cursor.close();
        return intent;
    }

    /**
     * 根据页面名和资源Id查寻Intent
     * @param activityName
     * @param resId
     * @return 到达某个资源页面的Intent
     */
    public Intent queryIntent(String activityName,int resId){
        Log.i("LZH","aN: "+activityName+" resId: "+resId);
        Cursor cursor = database.query(ActivityData.tableName,null,
                ActivityData.ActivityName+"=?",
                new String[]{activityName},
                null,null,null);
        int tempResId = -1,targetActivityId = -1;

        while (cursor.moveToNext()){
            tempResId = cursor.getInt(cursor.getColumnIndex(ActivityData.ResId));
            if(tempResId == resId){
                targetActivityId = cursor.getInt(cursor.getColumnIndex(ActivityData.ActivityId));
                break;
            }
        }
        cursor.close();

        return queryIntent(targetActivityId,resId);
    }

    /**
     * 查找指定页面，指定次序的，播放指定资源的MotionEvent
     * @param activityName
     * @param resType
     * @param motionSeq
     * @return
     */
    public byte[] queryMotionEvent(String activityName,String resType,int motionSeq){
        byte[] eventBytes = null;
        Cursor cursor = database.query(MotionData.MotionTable,null,
                MotionData.ActivityName+"=? and "+MotionData.ResType+"=? and "+MotionData.MotionSeq+"=?",
                new String[]{activityName,resType,motionSeq+""},
                null,null,null);
        if(cursor.moveToNext()){
            String motionStr = cursor.getString(cursor.getColumnIndex(MotionData.MotionByte));
            if(motionStr==null){
                Log.i("LZH","查询到的Motion字段为null");
            }
            eventBytes = Base64.decode(motionStr,0);
        }else{
            Log.i("LZH","未查询到对应的点击事件");
        }
        cursor.close();
        return eventBytes;
    }

    /**
     * 通过ActivityId查询activityName
     * @param activityId
     * @return
     */
    public String queryActivityNameByActivityId(int activityId){
        Cursor cursor = database.query(ActivityData.tableName,null,
                ActivityData.ActivityId+"=?",
                new String[]{activityId+""},
                null,null,null);
        String activityName = null;
        if(cursor.moveToNext()){
            activityName = cursor.getString(cursor.getColumnIndex(ActivityData.ActivityName));
        }else{
            Log.i("LZH","出错，为查询到ActivityName");
        }
        cursor.close();

        return activityName;
    }

    /**
     * 通过AppId 查询AppData
     * @param appId
     * @return
     */
    public AppData queryAppDataByAppId(int appId){
        AppData appData = null;
        Cursor cursor = database.query(AppData.tableName,null,
                AppData.AppId+"=?",
                new String[]{appId+""},
                null,null,null);
        if(cursor.moveToNext()){
            appData = new AppData(cursor.getInt(cursor.getColumnIndex(AppData.AppId)),
                    cursor.getString(cursor.getColumnIndex(AppData.AppName)),
                    cursor.getString(cursor.getColumnIndex(AppData.Version)),
                    cursor.getString(cursor.getColumnIndex(AppData.PackageName)));
        }
        cursor.close();
        //一个AppID对应一个AppData
        return appData;
    }

    /**
     * 查找含有该页面的App
     * @param activityName
     * @return
     */
    public AppData queryAppDataByActivityName(String activityName){
        AppData appData = null;
        Cursor cursor = database.query(ActivityData.tableName,null,
                ActivityData.ActivityName+"=?",
                new String[]{activityName},
                null,null,null);
        int appId = -1;
        if(cursor.moveToNext()){
            appId = cursor.getInt(cursor.getColumnIndex(ActivityData.AppId));
            appData = queryAppDataByAppId(appId);
        }else {
            Log.i("LZH","出错：数据库中未保存此页面");
        }
        cursor.close();

        return appData;
    }

    /**
     * 通过ResId查询ActivityData(查询某个指定Id资源的页面)
     * 一个resId对应一个ActivityData
     * @param resId
     * @return
     */
    public ActivityData queryActivityDataByResId(int resId){
        Log.i("LZH","resId: "+resId);
        ActivityData activityData = null;
        AppData appData = null;
        Cursor cursor = database.query(ActivityData.tableName,null,
                ActivityData.ResId+"=?",
                new String[]{resId+""},
                null,null,null);

        if(cursor.moveToNext()){
            appData = queryAppDataByAppId(cursor.getInt(cursor.getColumnIndex(ActivityData.AppId)));
            activityData = new ActivityData(cursor.getInt(cursor.getColumnIndex(ActivityData.ActivityId)),
                    cursor.getString(cursor.getColumnIndex(ActivityData.ActivityName)),
                    cursor.getInt(cursor.getColumnIndex(ActivityData.AppId)),
                    appData,resId);
        }else{
            Log.i("LZH","通过资源Id查询页面失败");
        }
        cursor.close();

        return activityData;
    }

    /**
     * 返回所有的AppData
     * @return
     */
    public List<AppData> queryAllAppData(){
        List<AppData> list = new ArrayList<>();
        AppData appData = null;
        Cursor cursor = database.query(AppData.tableName,null, null, null,
                null,null,null);
        while (cursor.moveToNext()){
            appData = new AppData(cursor.getInt(cursor.getColumnIndex(AppData.AppId)),
                    cursor.getString(cursor.getColumnIndex(AppData.AppName)),
                    cursor.getString(cursor.getColumnIndex(AppData.Version)),
                    cursor.getString(cursor.getColumnIndex(AppData.PackageName)));
            list.add(appData);
        }
        cursor.close();

        return list;
    }

    /**
     * 通过AppId 来查询此App中的搜索资源，只有一个
     * @param appId
     * @return
     */
    public ResourceData querySearchResDataByAppId(int appId){
        ResourceData resourceData = null;
        Cursor cursor = database.query(ResourceData.ResourceTable,null,
                ResourceData.AppId+"=? and "+ResourceData.ResCategory+" =? ",
                new String[]{appId+"","搜索"},
                null,null,null);
        if(cursor.moveToNext()){
            resourceData = new ResourceData(cursor.getInt(cursor.getColumnIndex(ResourceData.ResId)),
                    cursor.getInt(cursor.getColumnIndex(ResourceData.AppId)),
                    cursor.getString(cursor.getColumnIndex(ResourceData.ResCategory)),
                    cursor.getString(cursor.getColumnIndex(ResourceData.ResEntityName)));
        }else{
            Log.i("LZH","没有查找到："+appId+" 对应的搜索资源 ");
        }
        cursor.close();

        return resourceData;
    }

    /**
     * 通过AppId,查询一个App中的所有资源
     * @param appId
     * @return
     */
    public List<ResourceData> queryResDataByAppId(int appId){
        List<ResourceData> resourceDatas = new ArrayList<>();
        ResourceData resourceData = null;
        Cursor cursor = database.query(ResourceData.ResourceTable,null,
                ResourceData.AppId+"=?",
                new String[]{appId+""},
                null,null,null);
        while (cursor.moveToNext()){
            resourceData = new ResourceData(cursor.getInt(cursor.getColumnIndex(ResourceData.ResId)),
                    cursor.getInt(cursor.getColumnIndex(ResourceData.AppId)),
                    cursor.getString(cursor.getColumnIndex(ResourceData.ResCategory)),
                    cursor.getString(cursor.getColumnIndex(ResourceData.ResEntityName)));
            resourceDatas.add(resourceData);
        }
        cursor.close();
        return resourceDatas;
    }

    /**
     * 查看一个App是否有这种资源类型
     * @param appId
     * @param resType
     * @return
     */
    public boolean checkAppContainResType(int appId,String resType){

        List<ResourceData> resourceDatas = queryResDataByAppId(appId);
        for(ResourceData resourceData:resourceDatas){
            if(resourceData.getResCategory().equals(resType)){
                return true;
            }
        }

        return false;
    }
}
