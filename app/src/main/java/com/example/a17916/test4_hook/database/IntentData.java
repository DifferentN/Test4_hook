package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;


public class IntentData {
    public static final String IntentTable = "IntentTable";
    public static final String IntentId = "IntentId"; //int auto prekey
    public static final String ActivityId = "ActivityId"; //int refer key
    public static final String ResId = "ResId"; // int refer key
    public static final String IntentByte = "IntentByte"; // TEXT


    private int intentId;

    private int activityId;

    private ActivityData activityData;

    private int resId;

    private ResourceData resourceData;

    //用来说明到达某一特殊页面的Intent

    private byte[] bytes;




    public IntentData(int intentId, int activityId, int resId, byte[] bytes) {
        this.intentId = intentId;
        this.activityId = activityId;
        this.resId = resId;
        this.bytes = bytes;
    }

    public int getIntentId() {
        return intentId;
    }

    public int getActivityId() {
        return activityId;
    }

    public ActivityData getActivityData() {
        return activityData;
    }

    public int getResId() {
        return resId;
    }

    public ResourceData getResourceData() {
        return resourceData;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setIntentId(int intentId) {
        this.intentId = intentId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setActivityData(ActivityData activityData) {
        this.activityData = activityData;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setResourceData(ResourceData resourceData) {
        this.resourceData = resourceData;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
