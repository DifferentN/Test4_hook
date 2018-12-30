package com.example.a17916.test4_hook.TempGenerateDataBase;

import android.util.Log;

import com.example.a17916.test4_hook.application.MyApplication;
import com.example.a17916.test4_hook.database.ActivityData;
import com.example.a17916.test4_hook.database.AppData;
import com.example.a17916.test4_hook.database.IntentData;
import com.example.a17916.test4_hook.database.ResourceData;
import com.greendao.gen.ActivityDataDao;
import com.greendao.gen.AppDataDao;
import com.greendao.gen.DaoSession;
import com.greendao.gen.IntentDataDao;
import com.greendao.gen.ResourceDataDao;

import java.util.List;

public class TestShowDataBase {
    private MyApplication myApplication;
    private DaoSession mDaoSession;
    public static TestShowDataBase testShowDataBase;

    public TestShowDataBase(){
        myApplication = MyApplication.getInstance();
        mDaoSession = myApplication.getDaoSession();
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
        AppDataDao appDataDao = mDaoSession.getAppDataDao();
        List<AppData> appDatas = appDataDao.loadAll();
        if(appDatas.size()<1){
            Log.i("LZH","没有App数据");
            return ;
        }
        Log.i("LZH","App数据   *********");
        for(AppData appData:appDatas){
            Log.i("LZH","AppId: "+appData.getAppId()+" App名称: "+appData.getAppName()+" App版本: "+appData.getVersion());
        }
        Log.i("LZH","App数据   *********");

    }

    private void showResourceData(){
        ResourceDataDao resourceDataDao = mDaoSession.getResourceDataDao();
        List<ResourceData> resDatas = resourceDataDao.loadAll();
        if(resDatas.size()<1){
            Log.i("LZH","没有资源数据");
            return ;
        }
        Log.i("LZH","资源数据   *********");
        for(ResourceData resData:resDatas){
            Log.i("LZH","资源Id: "+resData.getResId()+" AppId: "+resData.getAppId()+" 资源类别: "+resData.getResCategory()+" 资源名称: "+resData.getResEntityName());
        }
        Log.i("LZH","资源数据   *********");
    }

    private void showActivityData(){
        ActivityDataDao activityDataDao = mDaoSession.getActivityDataDao();
        List<ActivityData> activityDatas = activityDataDao.loadAll();
        if(activityDatas.size()<1){
            Log.i("LZH","没有资源页面映射数据");
            return ;
        }
        Log.i("LZH","源页面映射数据   *********");
        for(ActivityData activityData:activityDatas){
            String info = "页面Id: "+activityData.getActivityId()+" AppId: "+activityData.getAppId()+" 页面名称: "+activityData.getActivityName();
            List<ResourceData> resDatas = activityData.getResourceDatas();
            for(ResourceData resData:resDatas){
                Log.i("LZH",info+" 资源名称: "+resData.getResEntityName());
            }
        }
        Log.i("LZH","源页面映射数据   *********");
    }

    private void showIntentData(){
        IntentDataDao intentDataDao = mDaoSession.getIntentDataDao();
        List<IntentData> intentDatas = intentDataDao.loadAll();
        if(intentDatas.size()<1){
            Log.i("LZH","没有Intent数据");
            return ;
        }
        Log.i("LZH","Intent数据   *********");
        String info1 = "null",info2="NotNull";
        String info = null;
        for(IntentData intentData:intentDatas){
            info = intentData.getBytes()==null?info1:info2;
            Log.i("LZH","页面Id: "+intentData.getActivityId()+" 资源Id: "+intentData.getResId()+" intent:"+info);
        }
        Log.i("LZH","Intent数据   *********");
    }
}
