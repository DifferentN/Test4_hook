package com.example.a17916.test4_hook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.a17916.test4_hook.util.normal.ActivityUtil;
import com.example.a17916.test4_hook.util.normal.FileUtil;
//import android.support.annotation.Nullable;
//
//import com.socks.library.KLog;

//import ias.deepsearch.com.helper.util.BuildConfig;
//import ias.deepsearch.com.helper.util.normal.ActivityUtil;
//import ias.deepsearch.com.helper.util.normal.FileUtil;
/**
 * Created by vector on 16/7/11.
 */
public class DatasProvider extends ContentProvider{

    public static final String AUTHORITY = "com.example.a17916.test4_hook.provider";

    //获取页面跳转的信息，包括intent序列之类的
    private final static int TRANSFEROBJECT = 0;
    //抽取脚本
    private final static int EXTRACTOROBJECT = 1;
    //判断当前Activity是不是一个功能型的Activity
    private final static int HASFUNCTIONACTIVITY = 2;
    //将标记当前页面是否是跳转中
    private final static int RESETONJUMP = 3;
    //显示和关闭悬浮窗
    private final static int FLOATWINDOW = 4;
    //存储log信息到本地文件中
    private final static int SAVELOG = 5;
    private static UriMatcher mUriMatcher;



    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    static
    {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, "transfer_object", TRANSFEROBJECT);
        mUriMatcher.addURI(AUTHORITY, "extractor_object", EXTRACTOROBJECT);
        mUriMatcher.addURI(AUTHORITY, "has_function", HASFUNCTIONACTIVITY);
        mUriMatcher.addURI(AUTHORITY, "reset_jump", RESETONJUMP);
        mUriMatcher.addURI(AUTHORITY, "notify_float", FLOATWINDOW);
        mUriMatcher.addURI(AUTHORITY, "save_log", SAVELOG);
    }

    @Override
    public boolean onCreate() {
        sharedPreferences = getContext().getSharedPreferences("function", Context.MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(mUriMatcher.match(uri) == SAVELOG){
            //TOOD 保存log信息到本地文件中，这样可以和客户端相互通信了，后期是不是可以改为websocket？
            String content = sortOrder;
            String filename = selection;
            String className = projection[0];
            String packageName = projection[1];

            Log.i("LZH","SaveViewTree: "+sortOrder);

//            if(className.equals("com.didi.sdk.component.search.address.ctrl.SearchAddressActivity") || !ActivityUtil.isAppIsInBackground(getContext(), className, packageName)){
////                KLog.v(BuildConfig.GETVIEW, "======get view from: " + className + " =======");
////                KLog.v(BuildConfig.GETINTENT, "======get intent from: " + className + " =======");
////                KLog.v(BuildConfig.GETINTENT, "======get tree from: " + className + " =======");
//                //
//                FileUtil.writeFileToSD(filename, content);
//            }else{
////                KLog.v("liuyi", "not foreground: " + className);
//            }
        }
        return null;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
