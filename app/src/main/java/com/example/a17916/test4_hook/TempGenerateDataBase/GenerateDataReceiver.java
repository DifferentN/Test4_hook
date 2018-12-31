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
import com.example.a17916.test4_hook.util.normal.ParcelableUtil;
import com.greendao.gen.ActivityDataDao;
import com.greendao.gen.AppDataDao;
import com.greendao.gen.DaoSession;
import com.greendao.gen.IntentDataDao;
import com.greendao.gen.ResourceDataDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GenerateDataReceiver extends BroadcastReceiver {
    private final String taoPiaoPiaoFlimActivity = "com.taobao.movie.android.app.ui.filmdetail.FilmDetailActivity";
    private String activityName;
    private List<PageResult> resultList;
    private String appName;
    private String version;
    private String packageName;

    private Intent curIntent;

    private DaoSession mDaoSession;
    private MyApplication myApplication;
    private Context context;

    public GenerateDataReceiver(Context context){
        this.context = context;
        myApplication = (MyApplication) context.getApplicationContext();
        mDaoSession = myApplication.getDaoSession();
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
        }
    }

    private void addTaoPiaoPiaoFlimInfo(){
        AppData appData = addAppData(appName,version,packageName);

        ResourceData resData = null;
        for(PageResult pageResult:resultList){
            if(pageResult.getEntityType().equals("电影名称")){
                resData = addResData(appData,pageResult.getNodeValue(),pageResult.getEntityType());
            }
        }
        if(resData==null){
            Log.i("LZH","无法添加资源");
            return ;
        }
        ActivityData activityData = addJoinResActivity(appData,resData,activityName);

        if(activityData==null){
            Log.i("LZH","无法添加资源页面映射");
            return ;
        }

        addInentData(resData,activityData,curIntent);

        TestShowDataBase testShowDataBase = TestShowDataBase.getInstance();
        testShowDataBase.showData();
//        ResourceData resData = addResData(appData,);
    }

    /**
     * 添加一个新的APP记录
     * @param appName
     * @param version
     * @return
     */
    private AppData addAppData(String appName, String version,String pkName){
        AppData appData = null;
        AppDataDao appDataDao = mDaoSession.getAppDataDao();
        QueryBuilder queryBuilder = appDataDao.queryBuilder();
        queryBuilder.where(AppDataDao.Properties.AppName.eq(appName),
                AppDataDao.Properties.Version.eq(version),
                AppDataDao.Properties.PackageName.eq(pkName));

        List<AppData> apps = queryBuilder.list();
        if(apps.size()>=1){
            //有重复的App,去重
            int size = apps.size();
            for(int i=0;i<size-1;i++){
                apps.remove(1);
            }

            appData = apps.get(0);
        }else{
            //没有APP，添加APP
            long id = appDataDao.count()+1;
            appData = new AppData(id,appName,version,pkName);
            appDataDao.insert(appData);
        }

        return appData;
    }

    /**
     * 对一个App添加一个新的资源
     * @param appData 对应的APP
     * @param resEntityName 资源实体的名称
     * @param resType 资源的类型
     * @return 添加的资源条目
     */
    private ResourceData addResData(AppData appData,String resEntityName,String resType){
        ResourceData resourceData = null;
        long appId = appData.getAppId();
        ResourceDataDao resourceDataDao = mDaoSession.getResourceDataDao();
        QueryBuilder queryBuilder = resourceDataDao.queryBuilder();
        //利用APPId,资源类型，资源名称查找是否有资源粗存在
        queryBuilder.where(ResourceDataDao.Properties.AppId.eq(appId),
                ResourceDataDao.Properties.ResCategory.eq(resType),
                ResourceDataDao.Properties.ResEntityName.eq(resEntityName));
        List<ResourceData> resDatas = queryBuilder.list();
        if(resDatas.size()>=1){
            //App内有重复的资源，去重
            int size = resDatas.size();
            for(int i=0;i<size-1;i++){
                resDatas.remove(1);
            }
            resourceData = resDatas.get(0);
        }else{
            //没有对应的资源存在，添加资源
            long id = resourceDataDao.count()+1;
            resourceData = new ResourceData(id,appId,resType,resEntityName);
            resourceData.setAppId(appId);
            resourceDataDao.insert(resourceData);
            //给app添加对应资源
            appData.setResId(id);
        }
        return resourceData;
    }

    private ActivityData addJoinResActivity(AppData appData, ResourceData resourceData, String activityName){
        ActivityData activityData = null;
        List<ResourceData> resDatas = null;
        List<ActivityData> activityDatas = null;

        Long appId = appData.getAppId();
        Long resId = resourceData.getResId();
        ActivityDataDao activityDataDao = mDaoSession.getActivityDataDao();
        QueryBuilder queryBuilder = activityDataDao.queryBuilder();
        //用APP的Id和activity的名称查询是否 已经用指定activity去保存资源
        queryBuilder.where(ActivityDataDao.Properties.AppId.eq(appId),
                ActivityDataDao.Properties.ActivityName.eq(activityName));
        activityDatas = queryBuilder.list();

        if(activityDatas.size()<1){
            //小于1，表示还没有这个activity条目
            long id = activityDataDao.count()+1;
            activityData = new ActivityData(id,appId,activityName);
            activityDataDao.insert(activityData);

            resDatas = activityData.getResourceDatas();
            if(resDatas==null){
                //不能设置ResourceDatas
                Log.i("LZH","资源页面映射添加失败");
                return null;
//                resDatas = new ArrayList<>();
            }
            resDatas.add(resourceData);

//            activityDataDao.insert(activityData);
        }else if(activityDatas.size()==1){
            activityData = activityDatas.get(0);
            resDatas = activityDatas.get(0).getResourceDatas();

            //检查是否此页面是否可已含有此资源
            boolean hasResData= false;
            for(ResourceData resData:resDatas){
                if(resData.getResId().equals(resId)){
                    hasResData = true;
                }
            }
            Log.i("LZH","是否添加数据"+hasResData);
            //此页面未含有此资源，添加此资源
            if(!hasResData){
                resDatas.add(resourceData);
            }
        }else{
            Log.i("LZH","ActivityData表出现数据重复，出错");
        }
        return activityData;
    }

    private void addInentData(ResourceData resourceData,ActivityData activityData,Intent intent){
        List<IntentData> intentDatas;
        IntentData intentData;

        Long activityDataId = activityData.getActivityId();
        IntentDataDao intentDataDao = mDaoSession.getIntentDataDao();
        QueryBuilder queryBuilder = intentDataDao.queryBuilder();
        //利用ActivityDataId查询是否已经有到达对应资源页面的Intent
        queryBuilder.where(IntentDataDao.Properties.ActivityId.eq(activityDataId),
                IntentDataDao.Properties.ResId.eq(resourceData.getResId()));
        intentDatas = queryBuilder.list();

        if(intentDatas.size()<1){
            //没有到达对应资源页面的Intent
            Long id = intentDataDao.count()+1;
            intentData = new IntentData(id,activityDataId, resourceData.getResId(),ParcelableUtil.intent2Bytes(intent));
            intentDataDao.insert(intentData);
        }
    }
}
