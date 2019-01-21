package com.example.a17916.test4_hook.database;

import android.util.ArrayMap;

import java.util.ArrayList;

public class IntentParameter {
    public static final String tableName = "IntentParameterTable";
    public static final String IntentParameterId = "IntentParameterId";
    public static final String ParameterKey = "ParameterKey" ;
    public static final String ValueType = "ValueType";
    public static final String ParameterValue = "ParameterValue";//代表key对应的值，若key为基础类型则直接转化为String，若为对象则先转化为字节，再保存
    public static final String ActivityId = "ActivityId"; //int refer key
    public static final String ResId = "ResId"; // int refer key

    private int activityId ;
    private int resId ;
    private ArrayMap<String,String> KeyValueMap = null;
    public IntentParameter(int activityId,int resId){
        KeyValueMap = new ArrayMap<>();
        this.activityId = activityId;
        this.resId = resId;
    }

    public void addKeyAndValue(String key,String value){
        if(KeyValueMap == null){
            KeyValueMap = new ArrayMap<>();
        }
        KeyValueMap.put(key,value);
    }
    public String getValue(String key){
        return KeyValueMap.get(key);
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getActivityId() {
        return activityId;
    }

    public int getResId() {
        return resId;
    }
}
