package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


public class ResourceData {
    public static final String ResourceTable  = "ResourceTable";
    public static final String ResId = "ResId"; //int auto pre key
    public static final String AppId = "AppId"; // int refer key
    public static final String ResCategory = "ResCategory"; //text
    public static final String ResEntityName = "ResEntityName"; //text

    private int resId;

    private int appId;

    private AppData app;

    private String resCategory;

    private String resEntityName;

    private int activityId;

    private List<ActivityData> activityData;

    public ResourceData(int resId,int appId,String resCategory,String resEntityName){
        this.resId = resId;
        this.appId = appId;
        this.resCategory = resCategory;
        this.resEntityName = resEntityName;
    }

    public int getResId() {
        return resId;
    }

    public int getAppId() {
        return appId;
    }

    public AppData getApp() {
        return app;
    }

    public String getResCategory() {
        return resCategory;
    }

    public String getResEntityName() {
        return resEntityName;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setApp(AppData app) {
        this.app = app;
    }

    public void setResCategory(String resCategory) {
        this.resCategory = resCategory;
    }

    public void setResEntityName(String resEntityName) {
        this.resEntityName = resEntityName;
    }
}
