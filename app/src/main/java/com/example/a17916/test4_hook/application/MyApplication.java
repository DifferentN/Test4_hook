package com.example.a17916.test4_hook.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.a17916.test4_hook.database.MySQLiteDBHelper;

public class MyApplication extends Application {
    private static MyApplication mApp;
    private static MySQLiteDBHelper mySQLiteDBHelper;
    private static  SQLiteDatabase sqLiteDatabase;
    public static MyApplication getInstance(){
        return mApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        setDataBase();
    }
    private void setDataBase(){
        mySQLiteDBHelper = new MySQLiteDBHelper(this);
        sqLiteDatabase = mySQLiteDBHelper.getWritableDatabase();
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
    }
}
