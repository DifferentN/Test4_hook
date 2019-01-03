package com.example.a17916.test4_hook.receive;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.a17916.test4_hook.TempGenerateDataBase.GenerateDataService;
import com.example.a17916.test4_hook.TestGenerateTemple.AnalysePageRawDataTool;
import com.example.a17916.test4_hook.TestGenerateTemple.PageResult;
import com.example.a17916.test4_hook.TestGenerateTemple.TestGenerateTemple;
import com.example.a17916.test4_hook.activity.showResult.AnalyseResultActivity;
import com.example.a17916.test4_hook.monitorService.MonitorActivityService;
import com.example.a17916.test4_hook.view_data.MyViewNode;
import com.example.a17916.test4_hook.view_data.MyViewTree;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalActivityReceiver extends BroadcastReceiver {
    private Activity selfActivity;
    public static final String findView = "findView";
    public static final String intent = "intent";
    public static final String viewTree = "viewTree";
    public static final String currentActivity = "currentActivity";
    public static final String openTargetActivityByIntentInfo = "openTargetActivityByIntentInfo";
    public static final String openTargetActivityByIntent = "openTargetActivityByIntent";
    public static final String INPUT_TEXT = "INPUT_TEXT";
    public static final String TEXT_KEY = "TEXT_KEY";
    public static final String INPUT_EVENT = "INPUT_EVENT";
    public static final String EVENTS = "EVENTS";

    public static final String GenerateIntentData = "GenerateIntentData";

    public static final String fromActivityStart ="fromActivityStart";
    public static final String fromActivityPlay = "fromActivityPlay";
    public static final String TARGET_INTENT = "targetIntent";
    public static final String targetActivityName = "targetActivityName";
    public static final String targetPackageName = "targetPackageName";

    private String showActivityName = "";
    private String selfActivityName = "";
    private String curPackageName = "";
    private String textKey ;
    private byte[] eventBytes;
    private String startActivityFrom;

    private HashMap<String ,Integer> startActivity;
    private boolean isOpen,isAtSameApp;
    private String packageName,tarActivityName,dataUri;

    private int time = 0;
    private int eventTime = 0;
    public LocalActivityReceiver(Activity activity){
        selfActivity = activity;
        selfActivityName = activity.getComponentName().getClassName();
        startActivity = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case LocalActivityReceiver.viewTree:
//                Log.i("LZH","activity: "+selfActivityName);
                if(selfActivityName.equals(showActivityName)){
//                    Log.i("LZH","send View Tree  "+selfActivityName);
//                    sendViewTree(context);
                    ArrayList<PageResult> pageResults = analyseViewTree(context);

                    Intent openInfo = getShowIntent(pageResults);
                    selfActivity.startActivity(openInfo);
                    Log.i("LZH","open window");
                }else{
//                    Log.i("LZH","not open window "+selfActivityName+"  show "+showActivityName);
                }
                break;
            case LocalActivityReceiver.findView:
                break;
            case LocalActivityReceiver.currentActivity:
                Bundle bundle = intent.getBundleExtra("currentActivity");
                showActivityName = (String) bundle.get("showActivity");
                curPackageName = (String)bundle.get("curPackage");
//                Log.i("LZH",selfActivityName+" show activity: "+showActivityName);
                break;
            case LocalActivityReceiver.openTargetActivityByIntent:

                Intent tarIntent = intent.getParcelableExtra(LocalActivityReceiver.TARGET_INTENT);
                startActivityFrom = intent.getStringExtra(LocalActivityReceiver.fromActivityStart);
                Log.i("LZH","self: "+selfActivityName+"start: "+startActivityFrom);
                if(startActivityFrom.compareTo(selfActivityName)!=0){
                    break;
                }
                Log.i("LZH","从"+selfActivityName+"打开"+startActivityFrom);

                selfActivity.startActivity(tarIntent);
                break;
            case LocalActivityReceiver.INPUT_TEXT:
//                Log.i("LZH","InputText : "+time++);
                textKey = intent.getStringExtra(LocalActivityReceiver.TEXT_KEY);
                startActivityFrom = intent.getStringExtra(LocalActivityReceiver.fromActivityPlay);
//                Log.i("LZH","selfA: "+selfActivityName+" startF: "+startActivityFrom);
                if(startActivityFrom.compareTo(selfActivityName)!=0){
                    break;
                }
                Log.i("LZH"," to InputText");
                inputText(textKey);
                break;
            case LocalActivityReceiver.INPUT_EVENT:
                Log.i("LZH","intput_event: "+eventTime++);
                eventBytes = intent.getByteArrayExtra(LocalActivityReceiver.EVENTS);
                //在指定的页面播放点击事件
                startActivityFrom = intent.getStringExtra(LocalActivityReceiver.fromActivityPlay);
                if(startActivityFrom.compareTo(selfActivityName)!=0){
                    break;
                }
                playMotionEvent(eventBytes);
                break;
            case LocalActivityReceiver.GenerateIntentData:
                if(selfActivityName.equals(showActivityName)){
                    ArrayList<PageResult> pageResults = analyseViewTree(context);
                    //添加数据
                    Intent generateDataIntent = testGenerateData(pageResults);
                    selfActivity.sendBroadcast(generateDataIntent);
                }else{
//                    Log.i("LZH","not open window "+selfActivityName+"  show "+showActivityName);
                }
                break;
        }
    }


    private void inputText(String textKey) {
        View view = selfActivity.getWindow().getDecorView();
        EditText editText = findEditText(view);
        if(editText==null){
            Log.i("LZH","未找到EditText");
        }
        Log.i("LZH","输入text: "+textKey);
        editText.setText(textKey);
    }
    private EditText findEditText(View view){
        ArrayList<View> list = new ArrayList<>();
        list.add(view);
        View cur;
        ViewGroup viewGroup;
        while (!list.isEmpty()){
            cur = list.remove(0);
            if(cur instanceof ViewGroup){
                viewGroup = (ViewGroup) cur;
                for(int i=0;i<viewGroup.getChildCount();i++){
                    list.add(viewGroup.getChildAt(i));
                }
            }else if(cur instanceof EditText){
                return (EditText) cur;
            }
        }
        return null;
    }

    private Intent getShowIntent(ArrayList<PageResult> pageResults){
        Intent intent = new Intent();
//        ComponentName componentName = new ComponentName("com.example.a17916.test4_hook","com.example.a17916.test4_hook.activity.ShowActivity");
        ComponentName componentName = new ComponentName("com.example.a17916.test4_hook","com.example.a17916.test4_hook.activity.showResult.AnalyseResultActivity");
        intent.setComponent(componentName);
        intent.putParcelableArrayListExtra(AnalyseResultActivity.PageResult,pageResults);
        return  intent;
    }
    private ArrayList<PageResult> analyseViewTree(Context context){
        String res = "";

        Uri saveLogUri = Uri.parse("content://com.example.a17916.test4_hook.provider/save_log");
        View mDecorView = selfActivity.getWindow().getDecorView();
        ComponentName componentName= selfActivity.getComponentName();
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
        View mDecorView = selfActivity.getWindow().getDecorView();
        ComponentName componentName= selfActivity.getComponentName();
        MyViewTree viewTree = new MyViewTree(mDecorView,componentName.getPackageName(),componentName.getShortClassName());
        MyViewNode viewNode = viewTree.getViewNode();

        String acNames[] = componentName.getShortClassName().split("\\.");
        TestGenerateTemple testGenerateTemple = new TestGenerateTemple(componentName.getClassName(),componentName.getPackageName());
        testGenerateTemple.createTemplate(viewNode);

        context.getContentResolver().query(saveLogUri,new String[]{selfActivity.getClass().getName(), context.getPackageName()},"tree",null,viewNode.toString());
    }

    private void playMotionEvent(byte[] bytes) {
        //延时1s，回放点击事件，保证view已经被刷新出来
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MotionEvent[] motionEvents = tranformtoMotionEvent(bytes);
        MotionEvent curEvent;
        for(int i=0;i<motionEvents.length;i++){
            curEvent = MotionEvent.obtain(motionEvents[i]);
            selfActivity.dispatchTouchEvent(curEvent);
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

    //用来向数据库中添加数据
    private Intent testGenerateData(ArrayList<PageResult> pageResults){
        Intent addDataIntent = new Intent();
        addDataIntent.setAction(GenerateDataService.GENERATE_INTENT_DATA);
        addDataIntent.putExtra(GenerateDataService.ACTIVITY_NAME,selfActivityName);
        addDataIntent.putParcelableArrayListExtra(GenerateDataService.PAGE_INFO,pageResults);

        String appName = testGetAppName();
        String version = testGetAppVersion();

        addDataIntent.putExtra(GenerateDataService.VERSION,version);
        addDataIntent.putExtra(GenerateDataService.APP_NAME,appName);
        addDataIntent.putExtra(GenerateDataService.PACKAGE_NAME,selfActivity.getPackageName());

        return addDataIntent;
    }
    private String testGetAppName(){
        String pkName = selfActivity.getPackageName();
        PackageManager pm = selfActivity.getPackageManager();

        try {
            PackageInfo packageInfo = pm.getPackageInfo(pkName,0);
            int labelRes  = packageInfo.applicationInfo.labelRes;
//            Log.i("LZH","appName: "+selfActivity.getResources().getString(labelRes));
            return selfActivity.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String testGetAppVersion(){
        int version = -1;
        PackageInfo packageInfo;
        PackageManager pm = selfActivity.getPackageManager();
        try {
            packageInfo = pm.getPackageInfo(selfActivity.getPackageName(),PackageManager.GET_CONFIGURATIONS);
            version = packageInfo.versionCode;
            return version+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }


}
