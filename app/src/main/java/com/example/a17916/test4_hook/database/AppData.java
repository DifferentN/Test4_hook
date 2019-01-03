package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


public class AppData {
    public static final String tableName = "AppDataTable";
    public static final String AppId = "AppId"; //int auto pre key
    public static final String AppName = "AppName"; // Text
    public static final String Version = "Version"; //Text unique(AppName,Version)
    public static final String PackageName = "PackageName"; // Text

    private int appId;

    private String appName;

    private String version ;

    private String packageName;

//    private Long resId;
//
//    private List<ResourceData> resourceDatas;


    public AppData(int appId,String appName,String version,String packageName){
        this.appId = appId;
        this.appName = appName;
        this.version = version;
        this.packageName = packageName;
    }

    public int getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }

    public String getVersion() {
        return version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
