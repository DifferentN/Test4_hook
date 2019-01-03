package com.example.a17916.test4_hook.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;


public class MotionData {
    public static final String MotionTable = "MotionTable";
    public static final String MotionId = "MotionId"; // int auto pre key
    public static final String MotionSeq = "MotionSeq"; // int
    public static final String ActivityName = "ActivityName";// TEXT
    public static final String ResType = "ResType";// TEXT
    public static final String MotionByte = "MotionByte"; // TEXT

    private int motionId;

    private String activityName;

    private int motionSeq;

    private int activityId;

    private ActivityData activityData;

    //一类资源的打开操作可能相同,所以采用类别，而不是资源实体名称
    private String resCategory;

    //用来说明某一特殊页面的特征值

    private String contentKey;

    //用来说明到达某一特殊页面的操作

    private byte[] bytes;


    public MotionData(int motionId,String activityName,String resCategory,int motionSeq,byte[] bytes){
        this.motionId = motionId;
        this.activityName = activityName;
        this.resCategory = resCategory;
        this.motionSeq = motionSeq;
        this.bytes = bytes;
    }

    public String getActivityName() {
        return activityName;
    }

    public int getMotionSeq() {
        return motionSeq;
    }

    public int getActivityId() {
        return activityId;
    }

    public ActivityData getActivityData() {
        return activityData;
    }

    public String getResCategory() {
        return resCategory;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setMotionId(int motionId) {
        this.motionId = motionId;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setMotionSeq(int motionSeq) {
        this.motionSeq = motionSeq;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setActivityData(ActivityData activityData) {
        this.activityData = activityData;
    }

    public void setResCategory(String resCategory) {
        this.resCategory = resCategory;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
