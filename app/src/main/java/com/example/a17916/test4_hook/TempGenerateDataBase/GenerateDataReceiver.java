package com.example.a17916.test4_hook.TempGenerateDataBase;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.a17916.test4_hook.TestGenerateTemple.PageResult;
import com.example.a17916.test4_hook.application.MyApplication;
import com.example.a17916.test4_hook.database.ActivityData;
import com.example.a17916.test4_hook.database.AppData;
import com.example.a17916.test4_hook.database.IntentData;
import com.example.a17916.test4_hook.database.ResourceData;
import com.example.a17916.test4_hook.database.SaveManager;
import com.example.a17916.test4_hook.share.SavePreference;
import com.example.a17916.test4_hook.util.normal.IntentUtil;
import com.example.a17916.test4_hook.util.normal.ParcelableUtil;
import com.example.a17916.test4_hook.util.normal.SerializeUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class GenerateDataReceiver extends BroadcastReceiver {
    private final String douBanFlimActivity  ="com.douban.frodo.subject.activity.SubjectInterestsActivity";
    private final String taoPiaoPiaoFlimActivity = "com.taobao.movie.android.app.ui.filmdetail.FilmDetailActivity";
    private final String taoPiaoPiaoSearchActivity = "com.taobao.movie.android.app.search.MVGeneralSearchViewActivity";
    private final String taoPiaoPiaoArtistActivity = "com.taobao.movie.android.app.cineaste.ui.activity.ArtisteDetailActivity";
    private final String TempAddYiDaoData = "com.yongche.android.YDBiz.Order.HomePage.MainActivity";
    private String activityName;
    private List<PageResult> resultList;
    private String appName;
    private String version;
    private String packageName;

    private Intent curIntent;

    private MyApplication myApplication;
    private Context context;

    private SaveManager saveManager;
    public GenerateDataReceiver(Context context){
        this.context = context;
        myApplication = (MyApplication) context.getApplicationContext();
        saveManager = SaveManager.getInstance();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case GenerateDataService.GENERATE_INTENT_DATA:
                generateData(intent);
                break;
            case GenerateDataService.SAVE_INTENT:
                curIntent = intent.getParcelableExtra(GenerateDataService.NEED_SAVE_INTENT);
                tempSaveIntent(curIntent,intent.getStringExtra(GenerateDataService.ACTIVITY_NAME));
                break;

        }

    }

    private void generateData(Intent intent) {
        activityName = intent.getStringExtra(GenerateDataService.ACTIVITY_NAME);
        resultList = intent.getParcelableArrayListExtra(GenerateDataService.PAGE_INFO);
        appName = intent.getStringExtra(GenerateDataService.APP_NAME);
        version = intent.getStringExtra(GenerateDataService.VERSION);
        packageName = intent.getStringExtra(GenerateDataService.PACKAGE_NAME);

        switch (activityName){
            case taoPiaoPiaoFlimActivity:
                addTaoPiaoPiaoFlimInfo();
                break;
            case taoPiaoPiaoSearchActivity:
                addTaoPiaoPiaoSearch();
                break;
            case taoPiaoPiaoArtistActivity:
                addTaoPiaoPiaoArtist();
                break;
            case douBanFlimActivity:
                addBouBanFlimInfo();
                break;
            case TempAddYiDaoData:
                tempAddYiDaoData();
                break;
        }
    }

    private void tempAddYiDaoData() {
        //模拟添加一个易到的“地点数据”和
        AppData appData = saveManager.addAppData(appName,version,packageName);

        ResourceData resData = null;
        resData = saveManager.addResData(appData,"房山","地点");
//        resData = saveManager.addResData(appData,"AllEntity","搜索");
        if(resData==null){
            Log.i("LZH","无法添加资源");
            return ;
        }

//        addResToApp(resData,appData);

        ActivityData activityData = saveManager.addJoinResActivity(appData,resData,activityName);

        if(activityData==null){
            Log.i("LZH","无法添加资源页面映射");
            return ;
        }



        //只添加易到的搜索资源，不添加Intent
        TestShowDataBase testShowDataBase = TestShowDataBase.getInstance();
        testShowDataBase.showData();
    }

    private void addTaoPiaoPiaoFlimInfo(){
        AppData appData = saveManager.addAppData(appName,version,packageName);

        ResourceData resData = null;
        for(PageResult pageResult:resultList){
            if(pageResult.getEntityType().equals("电影名称")){
                resData = saveManager.addResData(appData,pageResult.getNodeValue(),pageResult.getEntityType());
            }
        }
        if(resData==null){
            Log.i("LZH","无法添加资源");
            return ;
        }

//        addResToApp(resData,appData);

        ActivityData activityData = saveManager.addJoinResActivity(appData,resData,activityName);

        if(activityData==null){
            Log.i("LZH","无法添加资源页面映射");
            return ;
        }

        saveManager.addInentData(resData,activityData,curIntent);

        TestShowDataBase testShowDataBase = TestShowDataBase.getInstance();
        testShowDataBase.showData();
    }

    private void addTaoPiaoPiaoSearch(){
        AppData appData = saveManager.addAppData(appName,version,packageName);

        ResourceData resData = null;
        resData = saveManager.addResData(appData,"AllEntity","搜索");
        if(resData==null){
            Log.i("LZH","无法添加资源");
            return ;
        }
        ActivityData activityData = saveManager.addJoinResActivity(appData,resData,activityName);

        if(activityData==null){
            Log.i("LZH","无法添加资源页面映射");
            return ;
        }

        saveManager.addInentData(resData,activityData,curIntent);

        TestShowDataBase testShowDataBase = TestShowDataBase.getInstance();
        testShowDataBase.showData();
    }

    private void addTaoPiaoPiaoArtist(){
        AppData appData = saveManager.addAppData(appName,version,packageName);

        ResourceData resData = null;
        for(PageResult pageResult:resultList){
            if(pageResult.getEntityType().equals("key艺人")){
                resData = saveManager.addResData(appData,pageResult.getNodeValue(),"艺人");
            }
        }
        if(resData==null){
            Log.i("LZH","无法添加资源");
            return ;
        }

//        addResToApp(resData,appData);

        ActivityData activityData = saveManager.addJoinResActivity(appData,resData,activityName);

        if(activityData==null){
            Log.i("LZH","无法添加资源页面映射");
            return ;
        }

        saveManager.addInentData(resData,activityData,curIntent);

        TestShowDataBase testShowDataBase = TestShowDataBase.getInstance();
        testShowDataBase.showData();
    }

    private void addBouBanFlimInfo() {
        AppData appData = saveManager.addAppData(appName,version,packageName);

        ResourceData resData = null;
        for(PageResult pageResult:resultList){
            if(pageResult.getEntityType().equals("电影名称")){
                resData = saveManager.addResData(appData,pageResult.getNodeValue(),pageResult.getEntityType());
            }
        }
        if(resData==null){
            Log.i("LZH","无法添加资源");
            return ;
        }

//        addResToApp(resData,appData);

        ActivityData activityData = saveManager.addJoinResActivity(appData,resData,activityName);

        if(activityData==null){
            Log.i("LZH","无法添加资源页面映射");
            return ;
        }

        saveManager.addInentData(resData,activityData,curIntent);


        TestShowDataBase testShowDataBase = TestShowDataBase.getInstance();
        testShowDataBase.showData();
    }



//    /**
//     * 给App添加一个资源，用来获得资源类型
//     * @param resourceData
//     * @param appData
//     */
//    private void addResToApp(ResourceData resourceData,AppData appData){
//        List<ResourceData> list = appData.getResourceDatas();
//        list.add(resourceData);
//    }

    private void analyseIntentParameterByJson(HashMap<String,String> maps, HashMap<String,String> typeMaps, JSONArray jsonArray, Intent intent){
        int jsonSize = jsonArray.size();
        JSON json = null;
        JSONArray childJSONArray;
        JSONObject childJSONObject;
        for(int i=0;i<jsonSize;i++){
            json = (JSON) jsonArray.get(i);
            if(json instanceof com.alibaba.fastjson.JSONObject){
                childJSONObject = (JSONObject) json;
                addJsonObject(maps,typeMaps,childJSONObject,intent);
            }else if(json instanceof JSONArray){
                childJSONArray = (JSONArray) json;
                addJsonArray(maps,typeMaps,childJSONArray,intent);
            }else{
                Log.i("LZH","第"+i+"个json 既不是jsonObject 也不是JsonArray");
            }
        }
    }

    private void addJsonObject(HashMap<String,String> maps, HashMap<String,String> typeMaps, com.alibaba.fastjson.JSONObject jsonObject,Intent intent){
        String valueType = null,key = null,value = null;
        key = jsonObject.getString("basic_name");
        valueType = jsonObject.getString("basic_type");
        value = IntentUtil.getValue(intent.getExtras(),key);
        if(value==null){
            Log.i("LZH","Intent 中不存在该对象，不添加到数据库中,key为: "+key);
            return;
        }else{
            maps.put(key,value);
        }
        typeMaps.put(key,valueType);
    }

    private void addJsonArray(HashMap<String,String> maps, HashMap<String,String> typeMaps, JSONArray jsonArray,Intent intent){
        String valueType = null,key = null,value = null;
        com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(0);
        key = jsonObject.getString("object_name");
        valueType = jsonObject.getString("object_type");

        value = IntentUtil.getValue(intent.getExtras(),key);
        if(value == null){
            Log.i("LZH","Intent 中不存在该对象，不添加到数据库中,key为: "+key);
            return;
        }else{
            maps.put(key,value);
        }
        typeMaps.put(key,valueType);
    }

    private JSONArray getDouBanFlimInfoJSON(){
        String str = "[{\"basic_name\":\"page_uri\",\"basic_type\":\"java.lang.String\",\"basic_value\":\"parameter1\"},{\"basic_name\":\"subject_uri\",\"basic_type\":\"java.lang.String\",\"basic_value\":\"parameter2\"},[{\"object_name\":\"com.douban.frodo.SUBJECT\",\"object_type\":\"com.douban.frodo.subject.model.subject.Subject\"},{\"attribute_name\":\"title\",\"attribute_type\":\"java.lang.String\",\"attribute_value\":\"parameter3\"}]]\n";
        JSONArray jsonArray = JSONArray.parseArray(str);
//        Log.i("LZH",jsonArray.toJSONString());
        return jsonArray;
    }

    private void tempSaveIntent(Intent intent,String activityName){
        SavePreference savePreference = SavePreference.getInstance(context);
        savePreference.writeIntent(activityName,intent);
        Log.i("LZH","测试保存Intent的key: "+activityName);
    }

}
