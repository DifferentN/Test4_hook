package com.example.a17916.test4_hook.database;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "database1.db";
    private static final int DB_VERSION = 1;
    private static final String createAppTable= "CREATE TABLE "+AppData.tableName+"("+
            AppData.AppId+" integer PRIMARY KEY AUTOINCREMENT, "+
            AppData.AppName+" TEXT, "+
            AppData.Version+" TEXT, "+
            AppData.PackageName+" TEXT, "+
            "unique( "+AppData.AppName+" , "+AppData.Version+")"+
            ");";
    private static final String createResourceTable= "CREATE TABLE "+ResourceData.ResourceTable+"("+
            ResourceData.ResId+" integer PRIMARY KEY AUTOINCREMENT, "+
            ResourceData.ResEntityName+" TEXT, "+
            ResourceData.ResCategory+" TEXT, "+
            ResourceData.AppId+" integer, "+
            "FOREIGN KEY( "+ResourceData.AppId+" ) REFERENCES "+AppData.tableName+" ( "+AppData.AppId+" )"+
            ");";
    //
    private static final String createAvtivityTable= "CREATE TABLE "+ActivityData.tableName+"("+
            ActivityData.ActivityId+" integer PRIMARY KEY AUTOINCREMENT, "+
            ActivityData.ActivityName+" TEXT , "+
            ActivityData.AppId+" integer , "+
            ActivityData.ResId+" integer, "+
            "unique( "+ActivityData.AppId+" , "+ActivityData.ActivityName+" , "+ActivityData.ResId+" ), "+
            "FOREIGN KEY( "+ActivityData.AppId+" ) REFERENCES "+AppData.tableName+" ( "+AppData.AppId+" ) , "+
            "FOREIGN KEY( "+ActivityData.ResId+" ) REFERENCES "+ResourceData.ResourceTable+" ( "+ResourceData.ResId+" )"+
            ");";
    private static final String createIntentTable= "CREATE TABLE "+IntentData.IntentTable+"("+
            IntentData.IntentId+" integer PRIMARY KEY AUTOINCREMENT, "+
            IntentData.IntentByte+" TEXT, "+
            IntentData.ActivityId+" integer, "+
            IntentData.ResId+" integer, "+
            "FOREIGN KEY( "+IntentData.ActivityId+" ) REFERENCES "+ActivityData.tableName+" ( "+ActivityData.ActivityId+" ) , "+
            "FOREIGN KEY( "+IntentData.ResId+" ) REFERENCES "+ResourceData.ResourceTable+" ( "+ResourceData.ResId+" )"+
            ");";
    private static final String createMotionTable= "CREATE TABLE "+MotionData.MotionTable+"("+
            MotionData.MotionId+" integer PRIMARY KEY AUTOINCREMENT, "+
            MotionData.MotionSeq+" integer, "+
            MotionData.MotionByte+" TEXT, "+
            MotionData.ActivityName+" TEXT, "+
            MotionData.ResType+" TEXT "+
            ");";
    private static final String createIntentParameterTable = "CREATE TABLE "+IntentParameter.tableName+" ( "+
            IntentParameter.IntentParameterId+" integer PRIMARY KEY AUTOINCREMENT, "+
            IntentParameter.ParameterKey+" TEXT , "+
            IntentParameter.ValueType+" TEXT , "+
            IntentParameter.ParameterValue+" TEXT , "+
            IntentParameter.ActivityId+" integer , "+
            IntentParameter.ResId+" integer , "+
            "FOREIGN KEY( "+IntentParameter.ActivityId+" ) REFERENCES "+ActivityData.tableName+" ( "+ActivityData.ActivityId+" ) , "+
            "FOREIGN KEY( "+IntentParameter.ResId+" ) REFERENCES "+ResourceData.ResourceTable+" ( "+ResourceData.ResId+" )"+
            ");";

    public MySQLiteDBHelper(Context context){
        this(context,DB_NAME,null,DB_VERSION);
    }

    public MySQLiteDBHelper( Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAppTable);
        db.execSQL(createResourceTable);
        db.execSQL(createAvtivityTable);
        db.execSQL(createIntentTable);
        db.execSQL(createMotionTable);
        db.execSQL(createIntentParameterTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
