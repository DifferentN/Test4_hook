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
        return showItems;
    }

}
