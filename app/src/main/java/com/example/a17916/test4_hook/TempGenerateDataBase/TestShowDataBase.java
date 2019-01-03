package com.example.a17916.test4_hook.TempGenerateDataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.a17916.test4_hook.application.MyApplication;
import com.example.a17916.test4_hook.database.ActivityData;
import com.example.a17916.test4_hook.database.AppData;
import com.example.a17916.test4_hook.database.IntentData;
import com.example.a17916.test4_hook.database.ResourceData;

import java.util.List;

public class TestShowDataBase {
    private MyApplication myApplication;
    private SQLiteDatabase database;
    public static TestShowDataBase testShowDataBase;

    public TestShowDataBase(){
        myApplication = MyApplication.getInstance();
        database = myApplication.getSqLiteDatabase();
    }

    public static TestShowDataBase getInstance(){
        if (testShowDataBase==null) {
            testShowDataBase = new TestShowDataBase();
        }
        return  testShowDataBase;
    }

    public void showData(){
        showAppData();
        showResourceData();
        showActivityData();
        showIntentData();


    }
    private void showAppData(){
        Cursor cursor = database.query(AppData.tableName,null,null,null,null,null,null);
        String appName,appVersion,appId;
        Log.i("LZH","App数据   *********");
        while (cursor.moveToNext()){
            appId = cursor.getInt(0)+"";
            appName = cursor.getString(1);
            appVersion = cursor.getString(2);
            Log.i("LZH","AppId: "+appId+" App名称: "+appName+" App版本: "+appVersion);
        }
        cursor.close();
    }

    private void showResourceData(){
        Cursor cursor = database.query(ResourceData.ResourceTable,null,null,null,null,null,null);
        String resId,resName,resType,appId;
        Log.i("LZH","资源数据   *********");
        while (cursor.moveToNext()){
            resId = cursor.getInt(0)+"";
            resName = cursor.getString(1);
            resType = cursor.getString(2);
            appId = cursor.getInt(3)+"";
            Log.i("LZH","资源Id: "+resId+" AppId: "+appId+" 资源类别: "+resType+" 资源名称: "+resName);
        }
        cursor.close();
    }

    private void showActivityData(){
        Cursor cursor = database.query(ActivityData.tableName,null,null,null,null,null,null);
        String activiyId,activiyName,appId,resId;
        Log.i("LZH","源页面映射数据   *********");
        while (cursor.moveToNext()){
            activiyId = cursor.getInt(0)+"";
            activiyName = cursor.getString(1);
            appId = cursor.getInt(2)+"";
            resId = cursor.getInt(3)+"";
            Log.i("LZH","页面Id: "+activiyId+"页面名称 : "+activiyName+" AppId: "+appId+" 资源Id "+resId);
        }
        Log.i("LZH","源页面映射数据   *********");
        cursor.close();
    }

    private void showIntentData(){
        Cursor cursor = database.query(IntentData.IntentTable,null,null,null,null,null,null);
        String intentId,activityId,resId;
        boolean hasByte = false;
        Log.i("LZH","Intent数据   *********");
        while (cursor.moveToNext()){
            intentId = cursor.getInt(0)+"";
            activityId = cursor.getInt(2)+"";
            resId = cursor.getInt(3)+"";
            if(cursor.getString(1)==null){
                hasByte = false;
            }else{
                hasByte = true;
            }
            Log.i("LZH","IntentId: "+intentId+" 页面Id: "+activityId+" 资源Id: "+resId+" intent:"+hasByte);

        }
        cursor.close();

        Log.i("LZH","Intent数据   *********");
    }
}
