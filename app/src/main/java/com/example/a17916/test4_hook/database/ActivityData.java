package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

public class ActivityData {
    public static final String tableName = "ActivityTable";
    public static final String ActivityId = "ActivityId"; // int auto
    public static final String ActivityName = "ActivityName"; //Text pre key
    public static final String ResId = "ResId"; // refer key ResourceData resId
    public static final String AppId = "AppId"; //int refer key AppData appId


    private int activityId;

    private String activityName;

    private int appId;

    private AppData appData;

    private int resId;

    private List<ResourceData> resourceDatas;

    public ActivityData(int activityId,String activityName,int appId,AppData appData,int resId){
        this.activityId = activityId;
        this.activityName = activityName;
        this.appId = appId;
        this.appData = appData;
        this.resId = resId;
    }

    public int getActivityId() {
        return activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getAppId() {
        return appId;
    }

    public AppData getAppData() {
        return appData;
    }

    public List<ResourceData> getResourceDatas() {
        return resourceDatas;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setAppData(AppData appData) {
        this.appData = appData;
    }

    public void setResourceDatas(List<ResourceData> resourceDatas) {
        this.resourceDatas = resourceDatas;
    }
}
