package com.example.a17916.test4_hook.activity.showResult;

import android.util.ArrayMap;

import java.util.HashMap;

public class AppMap {
    public static ArrayMap<String,String> activityAppMap;
    static {
        activityAppMap = new ArrayMap<>();
        activityAppMap.put("com.yongche.android.YDBiz.Order.HomePage.MainActivity","易到");
        activityAppMap.put("com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity","易到");
        activityAppMap.put("com.taobao.movie.android.app.oscar.ui.cinema.activity.ScheduleListRootActivity","淘票票");
    }
    public static String getAppByActivityName(String activityName){
        return activityAppMap.get(activityName);
    }
}
