package com.example.a17916.test4_hook.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.greendao.gen.DaoMaster;
import com.greendao.gen.DaoSession;

public class MyApplication extends Application {
    private static MyApplication mApp;
    private static DaoSession mDaoSession;

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
        //创建数据库mydb.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mApp,"myDB.db");
        //获取可写数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(database);
        //获取Dao对象管理者
        mDaoSession = daoMaster.newSession();

    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

}
