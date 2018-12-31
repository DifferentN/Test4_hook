package com.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.a17916.test4_hook.database.AppData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "APP_DATA".
*/
public class AppDataDao extends AbstractDao<AppData, Long> {

    public static final String TABLENAME = "APP_DATA";

    /**
     * Properties of entity AppData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property AppId = new Property(0, Long.class, "appId", true, "_id");
        public final static Property AppName = new Property(1, String.class, "appName", false, "APP_NAME");
        public final static Property Version = new Property(2, String.class, "version", false, "VERSION");
        public final static Property PackageName = new Property(3, String.class, "packageName", false, "PACKAGE_NAME");
        public final static Property ResId = new Property(4, Long.class, "resId", false, "RES_ID");
    }

    private DaoSession daoSession;


    public AppDataDao(DaoConfig config) {
        super(config);
    }
    
    public AppDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"APP_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: appId
                "\"APP_NAME\" TEXT," + // 1: appName
                "\"VERSION\" TEXT," + // 2: version
                "\"PACKAGE_NAME\" TEXT," + // 3: packageName
                "\"RES_ID\" INTEGER);"); // 4: resId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"APP_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AppData entity) {
        stmt.clearBindings();
 
        Long appId = entity.getAppId();
        if (appId != null) {
            stmt.bindLong(1, appId);
        }
 
        String appName = entity.getAppName();
        if (appName != null) {
            stmt.bindString(2, appName);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(3, version);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(4, packageName);
        }
 
        Long resId = entity.getResId();
        if (resId != null) {
            stmt.bindLong(5, resId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AppData entity) {
        stmt.clearBindings();
 
        Long appId = entity.getAppId();
        if (appId != null) {
            stmt.bindLong(1, appId);
        }
 
        String appName = entity.getAppName();
        if (appName != null) {
            stmt.bindString(2, appName);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(3, version);
        }
 
        String packageName = entity.getPackageName();
        if (packageName != null) {
            stmt.bindString(4, packageName);
        }
 
        Long resId = entity.getResId();
        if (resId != null) {
            stmt.bindLong(5, resId);
        }
    }

    @Override
    protected final void attachEntity(AppData entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AppData readEntity(Cursor cursor, int offset) {
        AppData entity = new AppData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // appId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // appName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // version
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // packageName
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4) // resId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AppData entity, int offset) {
        entity.setAppId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAppName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setVersion(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPackageName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setResId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AppData entity, long rowId) {
        entity.setAppId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AppData entity) {
        if(entity != null) {
            return entity.getAppId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AppData entity) {
        return entity.getAppId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
