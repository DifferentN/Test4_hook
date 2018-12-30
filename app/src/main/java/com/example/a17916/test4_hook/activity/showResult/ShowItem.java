package com.example.a17916.test4_hook.activity.showResult;

import android.content.Intent;

public class ShowItem {
    private Intent intent;
    private String appName;
    private String nodeValue;
    private String activityName;
    private String resourceType;

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getActivityName() {

        return activityName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public ShowItem(Intent intent, String appName, String nodeValue){
        this.intent = intent;
        this.appName = appName;
        this.nodeValue = nodeValue;
    }

    public Intent getIntent() {
        return intent;
    }

    public String getAppName() {
        return appName;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }
}
