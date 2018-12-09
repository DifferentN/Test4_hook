package com.example.a17916.test4_hook.receive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.a17916.test4_hook.TestGenerateTemple.AnalysePageRawDataTool;
import com.example.a17916.test4_hook.TestGenerateTemple.PageResult;
import com.example.a17916.test4_hook.TestGenerateTemple.TestGenerateTemple;
import com.example.a17916.test4_hook.activity.AnalyseResultActivity;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.view_data.MyViewNode;
import com.example.a17916.test4_hook.view_data.MyViewTree;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalActivityReceiver extends BroadcastReceiver {
    private Activity targetActivity;
    private Activity curActivity;
    public static final String findView = "findView";
    public static final String intent = "intent";
    public static final String viewTree = "viewTree";
    public static final String currentActivity = "currentActivity";
    public static final String openTargetActivityByIntentInfo = "openTargetActivityByIntentInfo";
    public static final String openTargetActivityByIntent = "openTargetActivityByIntent";

    private String showActivityName = "";
    private String selfActivityName = "";
    private String curPackageName = "";

    private HashMap<String ,Integer> startActivity;
    private boolean isOpen,isAtSameApp;
    private String packageName,tarActivityName,dataUri;

    public LocalActivityReceiver(Activity activity){
        targetActivity = activity;
        selfActivityName = activity.getComponentName().getClassName();
        startActivity = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case LocalActivityReceiver.viewTree:
                Log.i("LZH","activity: "+selfActivityName);
                if(selfActivityName.equals(showActivityName)){
                    Log.i("LZH","send View Tree  "+selfActivityName);
//                    sendViewTree(context);
                    ArrayList<PageResult> pageResults = analyseViewTree(context);

                    Intent openInfo = getShowIntent(pageResults);
//                    openInfo.putExtra("info",info);
                    targetActivity.startActivity(openInfo);
                    Log.i("LZH","open window");
                }else{
                    Log.i("LZH","not open window "+selfActivityName+"  show "+showActivityName);
                }
                break;
            case LocalActivityReceiver.findView:
                break;
            case LocalActivityReceiver.currentActivity:
                Bundle bundle = intent.getBundleExtra("currentActivity");
                showActivityName = (String) bundle.get("showActivity");
                curPackageName = (String)bundle.get("curPackage");
                Log.i("LZH",selfActivityName+" show activity: "+showActivityName);
                break;
            case LocalActivityReceiver.openTargetActivityByIntentInfo:

                Bundle bundle1 = intent.getExtras();
                packageName = bundle1.getString("packageName");
                tarActivityName = bundle1.getString("tarActivityName");
                dataUri = bundle1.getString("dataUri");
                if(startActivity.get(tarActivityName)==null){
                    startActivity.put(tarActivityName,1);
                }else{
                    break;
                }
                Intent intent1 = new Intent();
//                packageName = "com.example.a17916.test3_1";
//                tarActivityName = "com.example.a17916.test3_1.SecondActivity";
                ComponentName component = new ComponentName(packageName,tarActivityName);
                Uri data = Uri.parse(dataUri);
                intent1.setComponent(component);
                intent1.setData(data);
                intent1.setAction("android.intent.action.VIEW");

                ComponentName componentName2 = targetActivity.getComponentName();
                isOpen = componentName2.getClassName().equals(tarActivityName);
                isAtSameApp = componentName2.getPackageName().equals(packageName);
                if(!isOpen&&isAtSameApp){
                    targetActivity.startActivity(intent1);
                }
                break;
            case LocalActivityReceiver.openTargetActivityByIntent:
                Intent tarIntent = intent.getParcelableExtra("opendActivityIntent");
//                packageName = "com.douban.movie";//com.yongche.android
//                tarActivityName = "com.douban.frodo.subject.activity.LegacySubjectActivity";
//                pkName = "com.yongche.android";

                packageName = intent.getStringExtra(MonitorActivityService.targetPackageName);
                tarActivityName = intent.getStringExtra(MonitorActivityService.targetActivityName);


//                tarActivityName = "com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity";
                //com.yongche.android.YDBiz.Order.DataSubpage.address.StartEndAddress.OSearchAddressEndActivity
                ComponentName c2 = targetActivity.getComponentName();
                isOpen = showActivityName.compareTo(tarActivityName)==0;
                isAtSameApp = curPackageName.compareTo(packageName)==0&&c2.getPackageName().compareTo(packageName)==0;
//                Log.i("LZH","tarActiName: "+c2.getClassName()+" tarPKName "+c2.getPackageName());
//                Log.i("LZH","IntentIndo: "+packageName+" tarAcName "+tarActivityName);
//                Log.i("LZH","if:"+isOpen+" "+isAtSameApp);
                if (!isOpen&&isAtSameApp) {
//                    Log.i("LZH","tarActiName: "+c2.getClassName()+" tarPKName "+c2.getPackageName());
//                    Log.i("LZH","IntentIndo: "+packageName+" tarAcName "+tarActivityName);
//                    Log.i("LZH","tarIntent: "+getIntentInfo(tarIntent));

                    //针对豆瓣电影设置，从主页面启动
//                    if(showActivityName.contains("MainActivity")&&showActivityName.compareTo(c2.getClassName())==0){
//                        targetActivity.startActivity(tarIntent);
//                    }
                    if(showActivityName.contains("Main")&&showActivityName.compareTo(c2.getClassName())==0){
                        targetActivity.startActivity(tarIntent);
                    }


//                    Log.i("LZH","open: "+getIntentInfo(tarIntent));
                }else if(isOpen&&showActivityName.compareTo(c2.getClassName())==0){
                    //&&showActivityName.compareTo(c2.getClassName())==0
                    Log.i("LZH","selfActivity Name: "+c2.getClassName()+"replay TouchEvent");
                    notifyActivityHasOpened(targetActivity);
                    Bundle bundle2 = intent.getBundleExtra("event");
                    byte[] bytes = bundle2.getByteArray("motionEvents");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    playMotionEvent(bytes);
                }

                break;
        }
    }

    private void notifyActivityHasOpened(Activity activity){
        Intent intent = new Intent();
        intent.setAction(MonitorActivityService.opened);
//        intent.setAction(OpenActivityService.testHasOpen);
        activity.sendBroadcast(intent);

    }
    private Intent getShowIntent(ArrayList<PageResult> pageResults){
        Intent intent = new Intent();
//        ComponentName componentName = new ComponentName("com.example.a17916.test4_hook","com.example.a17916.test4_hook.activity.ShowActivity");
        ComponentName componentName = new ComponentName("com.example.a17916.test4_hook","com.example.a17916.test4_hook.activity.AnalyseResultActivity");
        intent.setComponent(componentName);
        intent.putParcelableArrayListExtra(AnalyseResultActivity.PageResult,pageResults);
        return  intent;
    }
    private ArrayList<PageResult> analyseViewTree(Context context){
        String res = "";

        Uri saveLogUri = Uri.parse("content://com.example.a17916.test4_hook.provider/save_log");
        View mDecorView = targetActivity.getWindow().getDecorView();
        ComponentName componentName= targetActivity.getComponentName();
        MyViewTree viewTree = new MyViewTree(mDecorView,componentName.getPackageName(),componentName.getShortClassName());
        MyViewNode viewNode = viewTree.getViewNode();

        AnalysePageRawDataTool analysePageRawDataTool = AnalysePageRawDataTool.newInstance();
        ArrayList<PageResult> pageResults = analysePageRawDataTool.getResultByActivityName(componentName.getClassName(),viewNode);
        for(PageResult pageResult:pageResults){
            res+=pageResult.getEntityType()+": "+pageResult.getNodeValue()+"\n";
//            Log.i("LZH","vlaue: "+pageResult.getNodeValue()+" ; entityType: "+pageResult.getEntityType());
        }
        if(res.equals("")){
            res = "null";
        }
        return pageResults;

    }

    private void sendViewTree(Context context){

        Uri saveLogUri = Uri.parse("content://com.example.a17916.test4_hook.provider/save_log");
        View mDecorView = targetActivity.getWindow().getDecorView();
        ComponentName componentName= targetActivity.getComponentName();
        MyViewTree viewTree = new MyViewTree(mDecorView,componentName.getPackageName(),componentName.getShortClassName());
        MyViewNode viewNode = viewTree.getViewNode();

        String acNames[] = componentName.getShortClassName().split("\\.");
        TestGenerateTemple testGenerateTemple = new TestGenerateTemple(componentName.getClassName(),componentName.getPackageName());
        testGenerateTemple.createTemplate(viewNode);

        context.getContentResolver().query(saveLogUri,new String[]{targetActivity.getClass().getName(), context.getPackageName()},"tree",null,viewNode.toString());
    }

    private String getIntentInfo(Intent intent){
        String res = "";
        ComponentName componentName = intent.getComponent();

        String tarActivity = componentName.getClassName();
        String action = intent.getAction();
        String type = intent.getType();
        String data = intent.getDataString();
        String scheme = intent.getScheme();
        return res+="action: "+action+" type: "+type+" data: "+data+" scheme: "+scheme+" tarActivity: "+tarActivity;
    }
    private void playMotionEvent(byte[] bytes) {
        MotionEvent[] motionEvents = tranformtoMotionEvent(bytes);
        MotionEvent curEvent;
        for(int i=0;i<motionEvents.length;i++){
            curEvent = MotionEvent.obtain(motionEvents[i]);
            targetActivity.dispatchTouchEvent(curEvent);
//            targetActivity.dispatchTouchEvent(motionEvents[i]);
            Log.i("LZH","x: "+curEvent.getRawX()+" y: "+curEvent.getRawY());
        }
    }
    private MotionEvent[] tranformtoMotionEvent(byte[] bytes){
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes,0,bytes.length);
        parcel.setDataPosition(0);
        int size = parcel.readInt();
        MotionEvent[] motionEvents = new MotionEvent[size];
        parcel.readTypedArray(motionEvents,MotionEvent.CREATOR);
        return motionEvents;

    }
}
