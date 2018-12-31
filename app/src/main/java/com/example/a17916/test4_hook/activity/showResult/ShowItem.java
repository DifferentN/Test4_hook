package com.example.a17916.test4_hook.activity.showResult;

import android.content.Intent;

public class ShowItem {
    private Long resourceId;
    private String resourceType;
    private String resEntityName;//资源实体名称
    private String appName;
    private String pkName;
    private String activityName;

    public ShowItem(Long resourceId,String resourceType,String resEntityName,String appName,String pkName, String activityName){
        this.resourceId = resourceId;
        this.resourceType = resourceType;
        this.resEntityName = resEntityName;
        this.appName = appName;
        this.pkName = pkName;
        this.activityName = activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getActivityName() {

        return activityName;
    }

    public String getPkName() {
        return pkName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getAppName() {
        return appName;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public String getResEntityName() {
        return resEntityName;
    }

    public void setResEntityName(String resEntityName) {
        this.resEntityName = resEntityName;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
