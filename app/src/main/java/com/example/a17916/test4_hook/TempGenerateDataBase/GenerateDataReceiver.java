package com.example.a17916.test4_hook.TempGenerateDataBase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.a17916.test4_hook.TestGenerateTemple.PageResult;
import com.example.a17916.test4_hook.application.MyApplication;
import com.example.a17916.test4_hook.database.ActivityData;
import com.example.a17916.test4_hook.database.AppData;
import com.example.a17916.test4_hook.database.IntentData;
import com.example.a17916.test4_hook.database.ResourceData;
import com.example.a17916.test4_hook.database.SaveManager;
import com.example.a17916.test4_hook.util.normal.ParcelableUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GenerateDataReceiver extends BroadcastReceiver {
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



    /**
     * 给App添加一个资源，用来获得资源类型
     * @param resourceData
     * @param appData
     */
//    private void addResToApp(ResourceData resourceData,AppData appData){
//        List<ResourceData> list = appData.getResourceDatas();
//        list.add(resourceData);
//    }
}
