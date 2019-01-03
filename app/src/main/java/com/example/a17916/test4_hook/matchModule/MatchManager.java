package com.example.a17916.test4_hook.matchModule;

import android.util.Log;

import com.example.a17916.test4_hook.activity.showResult.ShowItem;
import com.example.a17916.test4_hook.database.ActivityData;
import com.example.a17916.test4_hook.database.AppData;
import com.example.a17916.test4_hook.database.QueryManager;
import com.example.a17916.test4_hook.database.ResourceData;

import java.util.ArrayList;
import java.util.List;

public class MatchManager {
    private static MatchManager matchManager;
    private QueryManager queryManager;
    public MatchManager(){
        queryManager = QueryManager.getInstance();
    }
    public static MatchManager getInstance(){
        if(matchManager==null){
            matchManager = new MatchManager();
        }
        return matchManager;
    }

    public List<ShowItem> queryItem(String resEntityName,String resType){
        ArrayList<ShowItem> showItems = new ArrayList<>();
        List<ResourceData> list = queryManager.queryResDataByName(resEntityName);


        AppData appData = null;
        ActivityData activityData = null;
        ShowItem showItem = null;
        for(ResourceData resourceData:list){
            appData = queryManager.queryAppDataByAppId(resourceData.getAppId());
            activityData = queryManager.queryActivityDataByResId(resourceData.getResId());
            showItem = new ShowItem(resourceData.getResId(),resourceData.getResCategory(),resourceData.getResEntityName(),
                    appData.getAppName(),appData.getPackageName(),activityData.getActivityName());
            showItems.add(showItem);
        }

        //添加执行搜索的Item
        List<ShowItem> searchItems = querySearchItem(resEntityName,resType);
        showItems.addAll(searchItems);

        return showItems;
    }

    /**
     * 查询含有此类型资源的App,并创造“搜索”Item
     * @param resEntityName
     * @param resType
     * @return
     */
    private List<ShowItem> querySearchItem(String resEntityName,String resType){
        ArrayList<ShowItem> searchItems = new ArrayList<>();
        List<AppData> appDatas = queryAppDataByResType(resType);

        ResourceData resourceData = null;
        ActivityData activityData = null;
        ShowItem showItem = null;

        for(AppData appData:appDatas){
            Log.i("LZH","app "+appData.getAppName()+"含有："+resType);
            resourceData = queryManager.querySearchResDataByAppId(appData.getAppId());
            if(resourceData==null){
                Log.i("LZH","此App没有对应数据查找下一个");
                continue;
            }
            activityData = queryManager.queryActivityDataByResId(resourceData.getResId());
            showItem = new ShowItem(resourceData.getResId(),resourceData.getResCategory(),
                    resEntityName,appData.getAppName(),appData.getPackageName(),activityData.getActivityName());
            searchItems.add(showItem);
            Log.i("LZH","app: "+appData.getAppName()+" resType: "+resourceData.getResCategory()+" activityName: "+activityData.getActivityName());
        }
        return searchItems;
    }

    /**
     * 获取包含指定资源类型的AppData
     * @return
     */
    private List<AppData> queryAppDataByResType(String resType){
        List<AppData> result = new ArrayList<>();
        List<AppData> appDatas = queryManager.queryAllAppData();
        List<ResourceData> resourceDatas = null;

        for(AppData appData:appDatas){
            resourceDatas = queryManager.queryResDataByAppId(appData.getAppId());
            for(ResourceData resourceData:resourceDatas){
                if(resourceData.getResCategory().equals(resType)){
                    result.add(appData);
                    break;
                }
            }
        }
        return result;
    }


}
