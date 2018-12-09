package com.example.a17916.test4_hook.util.activityView;

import android.app.Activity;
import android.content.ComponentName;
import android.util.Log;

import java.util.ArrayList;

public class ActivityCache {
    private static ArrayList<ActivityNode> list = new ArrayList<>();//保存已打开的activity，第一个为正在显示的
    public static ActivityCache activityCache ;
    public static ActivityCache getInstance(){
        if(activityCache==null){
            activityCache = new ActivityCache();
            Log.i("LZH","new ActivityCache");
        }
        return activityCache;
    }
    public void add(Activity activity){
        String flagName = getFlagName(activity);
        boolean isAdded = false;
        int size=list.size();
        ActivityNode node;
        for(int i=0;i<size;i++){
            node = list.get(i);
            if(node.flagName.equals(flagName)){
                isAdded = true;
                list.remove(i);
                list.add(0,node);
                break;
            }
        }
        if(!isAdded){
            list.add(0,new ActivityNode(flagName,activity));
        }
        Log.i("LZH","list add size is "+list.size());
    }
    public void remove(Activity activity){
        String flagName = getFlagName(activity);
        int size=list.size();
        ActivityNode node;
        for(int i=0;i<size;i++){
            node = list.get(i);
            if(node.flagName.equals(flagName)){
                list.remove(i);
                break;
            }
        }
        Log.i("LZH","list remove size is "+list.size());
    }
    public ActivityNode getFirst(){
        return list.get(0);
    }
    private String getFlagName(Activity activity){
        ComponentName componentName = activity.getComponentName();
        String className = componentName.getClassName();
        String PackageName = componentName.getPackageName();
        String flagName = className+"/"+PackageName;
        return flagName;
    }
    public static class ActivityNode{
        public int id;
        private String flagName;//包名/类名；
        private Activity activity;//已经打开的activity
        private ActivityNode(String flagName,Activity activity){
            this.flagName = flagName;
            this.activity =activity;
        }

        public int getId() {
            return id;
        }

        public String getFlagName() {
            return flagName;
        }

        public Activity getActivity() {
            return activity;
        }
    }

}
